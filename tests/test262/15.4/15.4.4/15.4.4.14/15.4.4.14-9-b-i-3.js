  function testcase() 
  {
    try
{      Array.prototype[0] = false;
      return [true, ].indexOf(true) === 0;}
    finally
{      delete Array.prototype[0];}

  }
  {
    var __result1 = testcase();
    var __expect1 = true;
  }
  