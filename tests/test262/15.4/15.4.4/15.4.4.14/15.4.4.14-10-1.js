// XXX
//  function testcase() 
//  {
//    var a = new Array();
//    a[100] = 1;
//    a[99999] = "";
//    a[10] = new Object();
//    a[5555] = 5.5;
//    a[123456] = "str";
//    a[5] = 1000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000;
//    if (a.indexOf(1) !== 100 || a.indexOf("") !== 99999 || a.indexOf("str") !== 123456 || a.indexOf(1000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000) !== 5 || a.indexOf(5.5) !== 5555)
//    {
//      return false;
//    }
//    if (a.indexOf(true) === - 1 && a.indexOf(5) === - 1 && a.indexOf("str1") === - 1 && a.indexOf(null) === - 1 && a.indexOf(new Object()) === - 1)
//    {
//      return true;
//    }
//  }
//  {
//    var __result1 = testcase();
//    var __expect1 = true;
//  }
//  
