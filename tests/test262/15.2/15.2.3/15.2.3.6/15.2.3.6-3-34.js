  function testcase() 
  {
    var obj = {
      
    };
    var accessed = false;
    var arrObj = [];
    arrObj.enumerable = true;
    Object.defineProperty(obj, "property", arrObj);
    for(var prop in obj)
    {
      if (prop === "property")
      {
        accessed = true;
      }
    }
    return accessed;
  }
  {
    var __result1 = testcase();
    var __expect1 = true;
  }
  