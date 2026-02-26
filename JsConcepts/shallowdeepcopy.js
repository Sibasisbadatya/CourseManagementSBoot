 
// Sibasis Badatya --> because we created a new string in memory and assigned it to myName, instead of modifying the original string.

// Immutable vs Mutable

// Immutable means that the value cannot be changed after it is created. Mutable means that the value can be changed after it is created.


// primitives are immutable, so when we try to change them, we actually create a new value in memory and update the reference.
// Strings, numbers, booleans, null, undefined, symbols are all primitives and immutable in JavaScript.
// When we assign a primitive value to a variable, it holds the actual value. When we copy that variable to another variable, it creates a new copy of the value in memory.
//  So, when we change one variable, it does not affect the other variable because they are two separate copies of the value.


// In contrast, objects and arrays are mutable. When we assign an object or array to a variable, it holds a reference to the value in memory. 
// When we copy that variable to another variable, it creates a new reference to the same value in memory.
// So, when we change one variable, it affects the other variable because they both reference the same value in memory. 
// This is why we need to be careful when working with objects and arrays in JavaScript, as changes to one variable can have unintended consequences on other variables that reference the same value.

// Impure Functions
// An impure function is a function that has side effects or relies on external state.
// For example, a function that modifies a global variable or a function that makes an API call is an impure function. 
// Impure functions can lead to bugs and make it harder to reason about code, as they can have unintended consequences on other parts of the codebase. 
// It is generally recommended to write pure functions, which are functions that do not have side effects and always return the same output for the same input.

const addToScore = (array, score) => {
        array.push(score);
        return array;
}

const scores = [10, 20, 30];
const newScores = addToScore(scores, 40);
console.log("Score",scores);

// To avoid this, we can create a new array instead of modifying the original array. This is called a shallow copy.

const addToScore2 = (array, score) => {
        const newArray = [...array];
        newArray.push(score);
        return newArray;
}

const scores2 = [10, 20, 30];
const newScores2 = addToScore2(scores2, 40);
console.log("Score2",scores2);



// Shallow Copy vs Deep Copy

const score3 = Object.assign([],scores2); //object.assign creates a shallow copy of the array. 
// It creates a new array, but the elements inside the array are still references to the same objects in memory. 
// So, if we modify an object inside the new array, it will also modify the original array.
// [] means we are creating a new empty array and then we are copying the elements of the original array into the new array.
console.log("score3",score3);

console.log(score3 === scores2);

// why they are not the same? because we created a new array using object.assign, but the elements inside the array are still references to the same objects in memory.
// still references to the same objects in memory proove me

// Shallow Copy VS Deep Copy

// A shallow copy creates a new object, but it does not create a new copy of the objects inside the original object.
// meaning only make a new copy of the top-level object, but the nested objects are still shared between the original and the copied object.
// A deep copy creates a new object and also creates new copies of the objects inside the original object.

const user1 = {
        name: "Sibasis",
        age:30,
        address:{
            city:"Aska",
            state:"Odisha"
        }   
}

const user2 = Object.assign({},user1); //shallow copy
user2.name = "Sibasis Badatya";
console.log("user2",user2);
console.log("user1",user1);

user2.address.city = "Bhubaneswar";
console.log("user2",user2);
console.log("user1",user1);

// best way to create shallow copy of an object is to use the spread operator. It creates a new object and copies the properties of the original object into the new object.

const user3 = {
        ...user1,
        address:{
            ...user1.address
        }
}









