  {
    var __result1 = true < 1 !== false;
    var __expect1 = false;
  }
  {
    var __result2 = 1 < true !== false;
    var __expect2 = false;
  }
  {
    var __result3 = new Boolean(true) < 1 !== false;
    var __expect3 = false;
  }
  {
    var __result4 = 1 < new Boolean(true) !== false;
    var __expect4 = false;
  }
  {
    var __result5 = true < new Number(1) !== false;
    var __expect5 = false;
  }
  {
    var __result6 = new Number(1) < true !== false;
    var __expect6 = false;
  }
  {
    var __result7 = new Boolean(true) < new Number(1) !== false;
    var __expect7 = false;
  }
  {
    var __result8 = new Number(1) < new Boolean(true) !== false;
    var __expect8 = false;
  }
  