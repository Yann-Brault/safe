  {
    var __result1 = ! (String.prototype.localeCompare.hasOwnProperty('length'));
    var __expect1 = false;
  }
  var __obj = String.prototype.localeCompare.length;
  String.prototype.localeCompare.length = (function () 
  {
    return "shifted";
  });
  {
    var __result2 = String.prototype.localeCompare.length !== __obj;
    var __expect2 = false;
  }
  