// XXX
//  function testcase() 
//  {
//    var obj = {
//      length : 1
//    };
//    try
//{      Object.prototype[0] = false;
//      Object.defineProperty(obj, "0", {
//        get : (function () 
//        {
//          return true;
//        }),
//        configurable : true
//      });
//      return 0 === Array.prototype.indexOf.call(obj, true);}
//    finally
//{      delete Object.prototype[0];}
//
//  }
//  {
//    var __result1 = testcase();
//    var __expect1 = true;
//  }
//  
