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
);

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
);

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
    (...args) => args.reduce((ans, x) => ans += x, 0),
    (varName, ...args) => new Add(...args.map((arg) => arg.diff(varName)))
);

function Subtract(...args) {
    Operation.call(this, ...args);
}

NewOperation(
    Subtract,
    "-",
    (x, y) => x - y,
    (varName, left, right) => new Subtract(left.diff(varName), right.diff(varName))
);

function Multiply(...args) {
    Operation.call(this, ...args);
}

NewOperation(
    Multiply,
    "*",
    (x, y) => x * y,
    (varName, left, right) => new Add(
            new Multiply(left.diff(varName), right),
            new Multiply(left, right.diff(varName))
        )
);

function Divide(...args) {
    Operation.call(this, ...args);
}

NewOperation(
    Divide,
    "/",
    (x, y) => x / y,
    (varName, left, right) => new Divide(
        new Subtract(
            new Multiply(left.diff(varName), right),
            new Multiply(left, right.diff(varName))
        ),
        new Multiply(right, right)
    )
);


function Negate(...args) {
    Operation.call(this, ...args);
}

NewOperation(
    Negate,
    "negate",
    (x) => -x,
    (varName, left) => new Negate(left.diff(varName))
);

function SumsqN(...args) {
    Operation.call(this, ...args);
}

NewOperation(
    SumsqN,
    "sumsq",
    (...args) => args.reduce((ans, x) => ans += x * x, 0),
    (varName, ...args) => new Add(...args.map((arg) => new Multiply(arg, arg).diff(varName)))
);

function NewSumsqN(sumsq, operationName) {
    sumsq.prototype = Object.create(SumsqN.prototype);
    sumsq.prototype.operationName = operationName;
}


function Sumsq2(...args) {
    SumsqN.call(this, ...args);
}

NewSumsqN(Sumsq2, "sumsq2");

function Sumsq3(...args) {
    SumsqN.call(this, ...args);
}

NewSumsqN(Sumsq3, "sumsq3");

function Sumsq4(...args) {
    SumsqN.call(this, ...args);
}

NewSumsqN(Sumsq4, "sumsq4");

function Sumsq5(...args) {
    SumsqN.call(this, ...args);
}

NewSumsqN(Sumsq5, "sumsq5");

function DistanceN(...args) {
    Operation.call(this, ...args);
}

NewOperation(
    DistanceN,
    "distance",
    (...args) => Math.sqrt(args.reduce((ans, x) => ans += x * x, 0)),
    function(varName, ...args) {
        return new Divide(new SumsqN(...args).diff(varName), new Multiply(new Const(2), this));
    }
);

function NewDistanceN(distance, operationName) {
    distance.prototype = Object.create(DistanceN.prototype);
    distance.prototype.operationName = operationName;
}

function Distance2(...args) {
    DistanceN.call(this, ...args);
}

NewDistanceN(Distance2, "distance2");

function Distance3(...args) {
    DistanceN.call(this, ...args);
}

NewDistanceN(Distance3, "distance3");

function Distance4(...args) {
    DistanceN.call(this, ...args);
}

NewDistanceN(Distance4, "distance4");

function Distance5(...args) {
    DistanceN.call(this, ...args);
}

NewDistanceN(Distance5, "distance5");

function AddN(obj, n, ...args) {
    obj.prototype.n = n;
    Operation.call(obj, ...args);
}

NewOperation(
    AddN,
    function() {
        return "add" + this.n;
    },
    (...args) => args.reduce((ans, a) => ans += a, 0),
    function(varName, ...args) {
        return new AddN(...args.map((a) => a.diff(varName)));
    }
)

function SumsqN(obj, n, ...args) {
    obj.prototype.n = n;
    Operation.call(obj, ...args);
}

NewOperation(
    SumsqN, 
    function() {
        return "sumsq" + this.n;
    },
    (...args) => args.reduce((a, ans) => ans += Math.pow(a, 2), 0),
    function(varName, ...args) {
        return new AddN(...args.map((a) => new Multiply(a, a).diff(varName)));
    }
);

function Sumsq2(obj, ...args) {
    SumsqN.call(obj, 2, ...args);
}

function Sumsq3(obj, ...args) {
    SumsqN.call(obj, 3, ...args);
}

function Sumsq4(obj, ...args) {
    SumsqN.call(obj, 4, ...args);
}

function Sumsq5(obj, ...args) {
    SumsqN.call(obj, 5, ...args);
}

function DistanceN(obj, n, ...args) {
    obj.prototype.n = n;
    Operation.call(obj, ...args);
}

NewOperation(
    DistanceN,
    function() {
        return "distance" + this.n;
    },
    (...args) => Math.sqrt(new SumsqN(...args)),
    function (varName, ...args) {
        return new Divide(new SumsqN(...args).diff(varName, ...args), 
            new Multiply(new Const(2), this));
    }
)

function Distance2(obj, ...args) {
    DistanceN.call(obj, 2, ...args);
}

function Distance3(obj, ...args) {
    DistanceN.call(obj, 3, ...args);
}

function Distance4(obj, ...args) {
    DistanceN.call(obj, 4, ...args);
}

function Distance5(obj, ...args) {
    DistanceN.call(obj, 5, ...args);
}

// function Distance2(...args) {
//     Operation.call(this, ...args);
// }



// NewOperation(
//     Distance2,
//     "distance2",
//     (x, y) => Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)),
//     function (varName, left, right) {
//         return new Divide(new Sumsq2(left, right).diff(varName, left, right), 
//             new Multiply(new Const(2), this));
//     }
// );

// function Distance3(...args) {
//     Operation.call(this, ...args);
// }

// NewOperation(
//     Distance3,
//     "distance3",
//     (x, y, z) => Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2)),
//     function (varName, a, b, c) {
//         return new Divide(new Sumsq3(a, b, c).diff(varName, a, b, c),
//             new Multiply(new Const(2), this));
//     }
// );

// function Distance4(...args) {
//     Operation.call(this, ...args);
// }

// NewOperation(
//     Distance4,
//     "distance4",
//     (a, b, c, d) => Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2) + Math.pow(c, 2) + Math.pow(d, 2)),
//     function (varName, a, b, c, d) {
//         return new Divide(new Sumsq4(a, b, c, d).diff(varName, a, b, c, d),
//             new Multiply(new Const(2), this));
//     } 
// );

// function Distance5(...args) {
//     Operation.call(this, ...args);
// }

// NewOperation(
//     Distance5,
//     "distance5",
//     (a, b, c, d, e) => Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2) + Math.pow(c, 2) + Math.pow(d, 2) + Math.pow(e, 2)),
//     function(varName, a, b, c, d, e) { 
//         return new Divide(new Sumsq5(a, b, c, d, e).diff(varName, a, b, c, d, e),
//             new Multiply(new Const(2), this))
//     }
// );

// function Sumsq2(...args) {
//     Operation.call(this, ...args);
// }


// NewOperation(
//     Sumsq2,
//     "sumsq2",
//     (x, y) => Math.pow(x, 2) + Math.pow(y, 2),
//     (varName, left, right) => new Add(new Multiply(left, left).diff(varName), 
//             new Multiply(right, right).diff(varName))
    
// );

// function Sumsq3(...args) {
//     Operation.call(this, ...args);
// }


// NewOperation(
//     Sumsq3,
//     "sumsq3",
//     (a, b, c) => Math.pow(a, 2) + Math.pow(b, 2) + Math.pow(c, 2),
//     (varName, a, b, c) => new Add(new Add(new Multiply(a, a).diff(varName), new Multiply(b, b).diff(varName)),
//             new Multiply(c, c).diff(varName))
    
// );

// function Sumsq4(...args) {
//     Operation.call(this, ...args);
// }


// NewOperation(
//     Sumsq4,
//     "sumsq4",
//     (a, b, c, d) => Math.pow(a, 2) + Math.pow(b, 2) + Math.pow(c, 2) + Math.pow(d, 2),
//     (varName, a, b, c, d) => new Add(new Add(new Add(new Multiply(a, a).diff(varName),
//             new Multiply(b, b).diff(varName)), new Multiply(c, c).diff(varName)),
//             new Multiply(d, d).diff(varName))
    
// );

// function Sumsq5(...args) {
//     Operation.call(this, ...args);
// }


// NewOperation(
//     Sumsq5,
//     "sumsq5",
//     (a, b, c, d, e) => Math.pow(a, 2) + Math.pow(b, 2) + Math.pow(c, 2) + Math.pow(d, 2) + Math.pow(e, 2),
//     (varName, a, b, c, d, e) => new Add(new Add(new Add(new Add(new Multiply(a, a).diff(varName),
//             new Multiply(b, b).diff(varName)), new Multiply(c, c).diff(varName)),
//             new Multiply(d, d).diff(varName)), new Multiply(e, e).diff(varName))
    
// );




const strToOperation = new Map([
    ["+", [Add, 2]],
    ["-", [Subtract, 2]],
    ["*", [Multiply, 2]],
    ["/", [Divide, 2]],
    ["negate", [Negate, 1]],
<<<<<<< Updated upstream
    ["sumsq2", [Sumsq2, 2]],
    ["sumsq3", [Sumsq3, 3]],
    ["sumsq4", [Sumsq4, 4]],
    ["sumsq5", [Sumsq5, 5]],
    ["distance2", [Distance2, 2]],
    ["distance3", [Distance3, 3]],
    ["distance4", [Distance4, 4]],
    ["distance5", [Distance5, 5]]
=======
    ["distance2", [Distance2, 2]],
    ["distance3", [Distance3, 3]],
    ["distance4", [Distance4, 4]],
    ["distance5", [Distance5, 5]],
    ["sumsq2", [Sumsq2, 2]],
    ["sumsq3", [Sumsq3, 3]],
    ["sumsq4", [Sumsq4, 4]],
    ["sumsq5", [Sumsq5, 5]]
>>>>>>> Stashed changes
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

<<<<<<< Updated upstream
=======
console.log(new Distance2(new Const(2), new Const(3)));

//console.log(new Distance2(new Const(2), new Const(3)).diff('x').toString())
// 2 * 2 + y * y = 2y
>>>>>>> Stashed changes
