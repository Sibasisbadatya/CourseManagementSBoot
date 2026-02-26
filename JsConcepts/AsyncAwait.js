function fetchData(callback) {
  setTimeout(() => {
    callback("Data received");
  }, 2000);
}

fetchData((data) => {
  console.log(data);
});

// calling async function using callbacks
// callback is a function that is passed as an argument to another function and is executed after some operation is completed.

// callback is a function that is passed as an argument to another function and is executed after some operation is completed.
// In the above example, we are passing a callback function to the fetchData function, which will be called after 2 seconds with the data received from the fetchData function.


const fetchPromiseData = () => {
    return new Promise((resolve,reject)=>{
        setTimeout(() => {
            resolve("Data received from Promise");
        }, 2000);
    })
}

fetchPromiseData().then((data)=>{
    console.log(data);
}).catch((error)=>{
    console.error(error);
});

// In the above example, we are creating a new Promise that will resolve after 2 seconds with the data received from the fetchPromiseData function. 
// We are then calling the then method on the Promise to log the data received from the Promise.
//  We are also catching any errors that may occur during the execution of the Promise.

async function sayHello() {
    return "Hello from async function";
}


// When we call an async function, it returns a Promise.
// So, we can use the then method to log the data received from the async function.


sayHello().then((data) => {
    console.log(data);
}).catch((error) => {
    console.error(error);
});


// testing excution order of then catch and async/await ***************************

function thenCatchTest() {
    return new Promise((resolve, reject) => {
        setTimeout(() => {
            resolve("Data received from thenCatchTest");
            
        }, 2000);
    })
}


console.log("Sibasis"); //step1 


//step 3
thenCatchTest().then((res)=>{    
    console.log("Then called", res);
}).catch((error)=>{
    console.log("Catch called", error);
})

// if above code is executed
// step 1 logged then 2 then 3


//step 3
 await thenCatchTest().then((res)=>{    
    console.log("Then called", res);
}).catch((error)=>{
    console.log("Catch called", error);
});

// step 1 logged then 3 then 2, because the await keyword is used to wait for the Promise to resolve before executing the next line of code, which is the console log.

console.log("Badatya"); //step 3



//  **********************************  FROM FRONTEND MASTERY ************************************************

function fakeAxios(){
    return new Promise((resolve,reject)=>{
        setTimeout(() => {
            resolve("Data received from fakeAxios");
        }, 2000);   
    })
}

const data = await fakeAxios();
console.log(data);


// https://youtu.be/zxswGsEtzYo?si=CwNzTqgnYAuimTyK 

// for detailed understanding of async/await, refer to the video above.