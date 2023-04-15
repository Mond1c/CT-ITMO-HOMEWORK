"use strict";

// :NOTE: зачем, если можно завести массив и делать в нём indexOf?
const varName = { 'x': 0, 'y': 1, 'z': 2 }

const variable = s => (...args) => args[varName[s]]
const cnst = num => () => num

let funcs = {}

function expressionCreator(functor, stringRepresentation) {
    let result = (...operands) => (x, y, z) => functor(...operands.map(op => op(x, y, z)))
    // :NOTE: можно и так вместо того, чтобы создать map и захардкодить туда арность операции (но второй способ тоже нормальный)
    result['countOfArgs'] = functor.length
    funcs[stringRepresentation] = result
    return result
}

const multiply = expressionCreator((a, b) => a * b, '*')
const divide = expressionCreator((a, b) => a / b, '/')
const subtract = expressionCreator((a, b) => a - b, '-')
const add = expressionCreator((a, b) => a + b, '+')
const negate = expressionCreator(a => -a, 'negate')

const madd = expressionCreator((a, b, c) => a * b + c, '*+')
const floor = expressionCreator(a => Math.floor(a), '_')
const ceil = expressionCreator(a => Math.ceil(a), '^')

const one = cnst(1)
const two = cnst(2)

let constants = {'one': one, 'two': two}

let parse = str => {
    let operands = []
    let tokens = str.split(' ').filter((x => x.length > 0))
    for (let idx = 0; idx < tokens.length; idx++) {
        if (tokens[idx] in funcs) {
            let countOfArgs = funcs[tokens[idx]]['countOfArgs']
            operands.push(funcs[tokens[idx]](...operands.splice(-countOfArgs, countOfArgs)))
        } else if (tokens[idx] in varName) {
            operands.push(variable(tokens[idx]))
        } else if (tokens[idx] in constants) {
            operands.push(constants[tokens[idx]])
        } else {
            operands.push(cnst(parseFloat(tokens[idx])))
        }
    }
    return operands[0]
}

let mainExpression = parse('x x * 2 x * - 1 +') // x^2 - 2x + 1
for (let x = 0; x <= 10; x++) {
    //console.log("x = " + x + ": " + mainExpression(x, 0, 0))
    print("x = " + x + ": " + mainExpression(x, 0, 0) + '\n')
}