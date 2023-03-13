"use strict";

const cnst = x => () => Number(x);

const variableToIndex = new Map([
    ["x", 0],
    ["y", 1],
    ["z", 2]
]);


const variable = varName => (...args) => args[variableToIndex.get(varName)];

const operation = operation => (...functions) => (...args) => operation(...functions.map(func =>
func(...args)));

const add = operation((left, right) => left + right);
const subtract = operation((left, right) => left - right);
const multiply = operation((left, right) => left * right);
const divide = operation((left, right) => left / right);
const negate = operation((left) => -left);

const strToOperation = new Map([
    ["+", [add, 2]],
    ["-", [subtract, 2]],
    ["*", [multiply, 2]],
    ["/", [divide, 2]],
    ["negate", [negate, 1]]
]);

const parseToken = (token, stack) => {
    if (strToOperation.has(token)) {
        return strToOperation.get(token);
    }
    stack.push(token);
    if (variableToIndex.has(token)) {
        return [variable, 1];
    }
    return [cnst, 1];
}


const parse = (expression) => {
    expression = expression.split(" ").filter(str => str !== "");
    const answer = expression.reduce((stack, str) => {
        const token = parseToken(str, stack);
        stack.push(token[0](...stack.splice(-token[1])));
        return stack;
    }, []);
    return answer.pop();
}


for (let i = 0; i <= 10; i++) {
    console.log(parse("x x * 2 x * - 1 +")(i));
}
