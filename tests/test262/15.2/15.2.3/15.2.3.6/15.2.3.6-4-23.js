  function testcase() 
  {
    var proto = {
      
    };
    Object.defineProperty(proto, "foo", {
      value : 11,
      configurable : false
    });
    var ConstructFun = (function () 
    {
      
    });
    ConstructFun.prototype = proto;
    var obj = new ConstructFun();
    Object.defineProperty(obj, "foo", {
      configurable : true
    });
    return obj.hasOwnProperty("foo") && (typeof obj.foo) === "undefined";
  }
  {
    var __result1 = testcase();
    var __expect1 = true;
  }
  