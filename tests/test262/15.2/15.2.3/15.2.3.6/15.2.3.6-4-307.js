//   TODO rewrite dataPropertyAttributesAreCorrect
//   function testcase() 
//   {
//     return (function () 
//     {
//       Object.defineProperty(arguments, "0", {
//         value : 0,
//         writable : false,
//         enumerable : true,
//         configurable : false
//       });
//       try
// {        Object.defineProperty(arguments, "0", {
//           enumerable : false
//         });}
//       catch (e)
// {        return e instanceof TypeError && dataPropertyAttributesAreCorrect(arguments, "0", 0, false, true, false);}
// 
//       return false;
//     })();
//   }
//   {
//     var __result1 = testcase();
//     var __expect1 = true;
//   }
//   
