document.getElementById('plotButton').addEventListener('click', () => {
    const input = document.getElementById('functionInput').value;

    const functions = input.split('\n').map(line => line.trim()).filter(line => line.startsWith('y ='));

    if (functions.length === 0) {
        alert('Введите хотя бы одну функцию в формате "y = ..."');
        return;
    }

    const traces = [];
    const XInput = document.getElementById('xInput').value;
    const YInput = document.getElementById('yInput').value;
    if(YInput == null){
        alert("Выберете значение y")
    }
    const Y = parseFloat(YInput);
    if(XInput == null){
        alert("Выберете значение x")
    }
    const X = parseFloat(XInput);


    functions.forEach((func) => {
        const expression = func.replace('y =', '').trim();

        try {
            const xValues = [];
            const yValues = [];
            for (let x = -10; x <= 10; x += 0.1) {
                xValues.push(x);
                const y = eval(expression.replace(/x/g, `(${x})`));
                yValues.push(y);
            }

            traces.push({
                x: xValues,
                y: yValues,
                mode: 'lines',
                name: `y = ${expression}`,
            });
        } catch (error) {
            alert(`Ошибка при разборе функции: ${func}. Проверьте синтаксис.`);
        }
    });

    Plotly.newPlot('plot', traces, {
        title: 'Графики функций',
        xaxis: { title: 'x' },
        yaxis: { title: 'y' },
    });

    sendFunctionsToServer(functions, X, Y);
});

const sendFunctionsToServer = async (functions, xValue, yValue) => {
    try {
        const response = await fetch('/web2-1.0-SNAPSHOT/dop', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                functions: functions,
                x: xValue,
                y: yValue
            }),
        });

        if (!response.ok) {
            throw new Error(`Ошибка HTTP: ${response.status}`);
        }

        const result = await response.json();
        console.log('Ответ от сервера:', result);

        const resultDopElement = document.getElementById('result-dop');
        const resultP = resultDopElement.querySelector('p');
        if (resultP) {
            resultP.textContent = result.answer || 'Нет данных из ответа';
        }
    } catch (error) {
        console.error('Ошибка при отправке функций и x на сервер:', error);

        const resultDopElement = document.getElementById('result-dop');
        const resultP = resultDopElement.querySelector('p');
        if (resultP) {
            resultP.textContent = 'Не удалось получить данные с сервера. Попробуйте позже.';
        }
    }
};