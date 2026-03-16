

function checkMembers(obj) {
    console.log("##### " + obj.constructor.name) // // <=> obj::class.simpleName
    Object
        .keys(obj.constructor.prototype) // <=> obj::class ou em Java obj.getClass()
        .forEach(name => {
            const member = obj[name]
            if(typeof member === "function")
                console.log("Func " + name)
            else
                console.log("Prop " + name)
        })
}

function checkAndCallMethods(obj) {
    console.log("##### " + obj.constructor.name) // // <=> obj::class.name
    Object
        .keys(obj.constructor.prototype) // <=> obj::class ou em Java obj.getClass()
        .map(name => obj[name])
        .filter(member => typeof member === "function" && member.length == 0)
        .forEach(func => {
            console.log("Func " + func.name + " =====> " + func.call(obj))
        })
}

function  MyObject(){
}
MyObject.prototype.a = 2
MyObject.prototype.b = "SLB"
MyObject.prototype.c = function xpto() { console.log("Glorioso!") }

//     a: 2,
//     b: "SLB",
//
// }

var obj = new MyObject()
console.log(obj.constructor)

checkMembers(obj)
checkAndCallMethods(obj)
checkAndCallMethods(new URL("https://github.com"))
checkAndCallMethods(new URL("https://isel.pt"))
checkAndCallMethods(performance)
