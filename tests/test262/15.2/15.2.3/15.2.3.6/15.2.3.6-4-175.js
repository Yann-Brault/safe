//   TODO getter/setter
//   function testcase() 
//   {
//     var arrObj = [0, 1, ];
//     try
// {      Object.defineProperty(Array.prototype, "1", {
//         get : (function () 
//         {
//           return 1;
//         }),
//         configurable : true
//       });
//       Object.defineProperty(arrObj, "length", {
//         value : 1
//       });
//       return arrObj.length === 1 && ! arrObj.hasOwnProperty("1");}
//     finally
// {      delete Array.prototype[1];}
// 
//   }
//   {
//     var __result1 = testcase();
//     var __expect1 = true;
//   }
//   
