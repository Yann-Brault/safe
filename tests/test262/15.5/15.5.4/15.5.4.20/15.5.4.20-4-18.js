  function testcase() 
  {
    return "abc\uFEFF".trim() === "abc";
  }
  {
    var __result1 = testcase();
    var __expect1 = true;
  }
  