//  TODO 15.9.3.1 new Date (year, month [, date [, hours [, minutes [, seconds [, ms]]]]])
//  var myObj = (function (val) 
//  {
//    this.value = val;
//    this.valueOf = (function () 
//    {
//      throw "valueOf-" + this.value;
//    });
//    this.toString = (function () 
//    {
//      throw "toString-" + this.value;
//    });
//  });
//  try
//{    var x1 = new Date(new myObj(1), new myObj(2), new myObj(3), new myObj(4), 
//    new myObj(5), 
//    new myObj(6));
//    $ERROR("#1: The 1st step is calling ToNumber(year)");}
//  catch (e)
//{    {
//      var __result1 = e !== "valueOf-1";
//      var __expect1 = false;
//    }}
//
//  try
//{    var x2 = new Date(1, new myObj(2), new myObj(3), new myObj(4), new myObj(5), 
//    new myObj(6));
//    $ERROR("#2: The 2nd step is calling ToNumber(month)");}
//  catch (e)
//{    {
//      var __result2 = e !== "valueOf-2";
//      var __expect2 = false;
//    }}
//
//  try
//{    var x3 = new Date(1, 2, new myObj(3), new myObj(4), new myObj(5), new myObj(6));
//    $ERROR("#3: The 3rd step is calling ToNumber(date)");}
//  catch (e)
//{    {
//      var __result3 = e !== "valueOf-3";
//      var __expect3 = false;
//    }}
//
//  try
//{    var x4 = new Date(1, 2, 3, new myObj(4), new myObj(5), new myObj(6));
//    $ERROR("#4: The 4th step is calling ToNumber(hours)");}
//  catch (e)
//{    {
//      var __result4 = e !== "valueOf-4";
//      var __expect4 = false;
//    }}
//
//  try
//{    var x5 = new Date(1, 2, 3, 4, new myObj(5), new myObj(6));
//    $ERROR("#5: The 5th step is calling ToNumber(minutes)");}
//  catch (e)
//{    {
//      var __result5 = e !== "valueOf-5";
//      var __expect5 = false;
//    }}
//
//  try
//{    var x6 = new Date(1, 2, 3, 4, 5, new myObj(6));
//    $ERROR("#6: The 6th step is calling ToNumber(seconds)");}
//  catch (e)
//{    {
//      var __result6 = e !== "valueOf-6";
//      var __expect6 = false;
//    }}
//
//  
