//   TODO getter/setter
//   function testcase() 
//   {
//     var obj = {
//       
//     };
//     var getFunc = (function () 
//     {
//       return 1001;
//     });
//     Object.defineProperty(obj, "prop", {
//       get : getFunc,
//       set : undefined,
//       enumerable : false,
//       configurable : false
//     });
//     var propertyDefineCorrect = obj.hasOwnProperty("prop");
//     var desc = Object.getOwnPropertyDescriptor(obj, "prop");
//     for(var p in obj)
//     {
//       if (p === "prop")
//       {
//         return false;
//       }
//     }
//     return propertyDefineCorrect && desc.enumerable === false;
//   }
//   {
//     var __result1 = testcase();
//     var __expect1 = true;
//   }
//   
