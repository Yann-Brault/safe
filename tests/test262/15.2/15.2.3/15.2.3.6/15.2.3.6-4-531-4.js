//   TODO getter/setter
//   function testcase() 
//   {
//     var obj = __Global;
//     try
// {      obj.verifySetFunction = "data";
//       Object.defineProperty(obj, "property", {
//         get : (function () 
//         {
//           return obj.verifySetFunction;
//         }),
//         set : (function (value) 
//         {
//           obj.verifySetFunction = value;
//         }),
//         configurable : true
//       });
//       obj.verifySetFunction1 = "data1";
//       var getFunc = (function () 
//       {
//         return obj.verifySetFunction1;
//       });
//       var setFunc = (function (value) 
//       {
//         obj.verifySetFunction1 = value;
//       });
//       Object.defineProperty(obj, "property", {
//         get : getFunc,
//         set : setFunc
//       });
//       return accessorPropertyAttributesAreCorrect(obj, "property", getFunc, setFunc, "verifySetFunction1", 
//       false, 
//       true);}
//     finally
// {      delete obj.property;
//       delete obj.verifySetFunction;
//       delete obj.verifySetFunction1;}
// 
//   }
//   {
//     var __result1 = testcase();
//     var __expect1 = true;
//   }
//   
