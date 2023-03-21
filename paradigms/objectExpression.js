"use strict";

const variableToIndex = new Map([
    ["x", 0],
    ["y", 1],
    ["z", 2]
]);

function PartOfExpression(obj, evaluate, diff, toString) {
    obj.prototype.evaluate = evaluate;
    obj.prototype.diff = diff;
    obj.prototype.toString = toString;
}

function Const(x) {
    this.x = x;
}


PartOfExpression(
    Const,
    function() {
        return Number(this.x);
    },
    function() {
        return new Const(0);
    },
    function() {
        return this.x + '';
    }
);

function Variable(varName) {
    this.varName = varName;
}

PartOfExpression(
    Variable,
    function(...args) {
        return args[variableToIndex.get(this.varName)];
    },
    function(varName) {
        return this.varName === varName ? new Const(1) : new Const(0);
    },
    function() {
        return this.varName;
    }
)

function Operation(...parts) {
    this.parts = parts;
}

PartOfExpression(
    Operation,
    function(...args) {
        return this.calculate(...this.parts.map(part => part.evaluate(...args)));
    },
    function(varName) {
        return this.diffOperation(varName, ...this.parts);
    },
    function() {
        return this.parts.map(part => part.toString()).join(" ") + " " + this.operationName;
    }
)

function NewOperation(operation, operationName, calculate, diffOperation) {
    operation.prototype = Object.create(Operation.prototype);
    operation.prototype.operationName = operationName;
    operation.prototype.calculate = calculate;
    operation.prototype.diffOperation = diffOperation;
}

function Add(...args) {
    Operation.call(this, ...args);
}

NewOperation(
    Add,
    "+",
    function(x, y) {
        return x + y;
    },
    function(varName, left, right) {
        return new Add(left.diff(varName), right.diff(varName));
    },
);

function Subtract(...args) {
    Operation.call(this, ...args);
}

NewOperation(
    Subtract,
    "-",
    function(x, y) {
        return x - y;
    },
    function(varName, left, right) {
        return new Subtract(left.diff(varName), right.diff(varName));
    }
);

function Multiply(...args) {
    Operation.call(this, ...args);
}

NewOperation(
    Multiply,
    "*",
    function(x, y) {
        return x * y;
    },
    function(varName, left, right) {
        return new Add(
            new Multiply(left.diff(varName), right),
            new Multiply(left, right.diff(varName))
        );
    }
)

function Divide(...args) {
    Operation.call(this, ...args);
}

NewOperation(
    Divide,
    "/",
    function(x, y) {
        return x / y;
    },
    function(varName, left, right) {
        return new Divide(
            new Subtract(
                new Multiply(left.diff(varName), right),
                new Multiply(left, right.diff(varName))
            ),
            new Multiply(right, right)
        );
    }
)


function Negate(...args) {
    Operation.call(this, ...args);
}

NewOperation(
    Negate,
    "negate",
    function(x) {
        return -x;
    },
    function(varName, left) {
        return new Negate(left.diff(varName));
    }
)

const strToOperation = new Map([
    ["+", [Add, 2]],
    ["-", [Subtract, 2]],
    ["*", [Multiply, 2]],
    ["/", [Divide, 2]],
    ["negate", [Negate, 1]]
]);

const parseToken = (token, stack) => {
    if (strToOperation.has(token)) {
        return strToOperation.get(token);
    }
    stack.push(token);
    if (variableToIndex.has(token)) {
        return [Variable, 1];
    }
    return [Const, 1];
}

const parse = (expression) => {
    return expression.split(" ").filter(str => str !== "").reduce(
        (stack, str) => {
            const token = parseToken(str, stack);
            stack.push(new token[0](...stack.splice(-token[1])));
            return stack;
        }, []
    ).pop();
}

for (let i = 0; i <= 10; i++) {
    console.log(parse("x x * 2 x * - 1 +").evaluate(i));
}