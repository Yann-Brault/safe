// TODO rewrite dataPropertyAttributesAreCorrect
// //   TODO getter/setter
// //   function testcase() 
// //   {
// //     var obj = [];
// //     obj.verifySetFunc = "data";
// //     var getFunc = (function () 
// //     {
// //       return obj.verifySetFunc;
// //     });
// //     var setFunc = (function (value) 
// //     {
// //       obj.verifySetFunc = value;
// //     });
// //     Object.defineProperty(obj, "0", {
// //       get : getFunc,
// //       set : setFunc,
// //       enumerable : true,
// //       configurable : true
// //     });
// //     var desc1 = Object.getOwnPropertyDescriptor(obj, "0");
// //     Object.defineProperty(obj, "0", {
// //       value : 1001
// //     });
// //     var desc2 = Object.getOwnPropertyDescriptor(obj, "0");
// //     return desc1.hasOwnProperty("get") && desc2.hasOwnProperty("value") && typeof desc2.get === "undefined" && typeof desc2.get === "undefined" && dataPropertyAttributesAreCorrect(obj, "0", 1001, false, true, true);
// //   }
// //   {
// //     var __result1 = testcase();
// //     var __expect1 = true;
// //   }
// //   
