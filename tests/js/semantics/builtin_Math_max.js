// Math.max
var __result1 = Math.max(NaN, 1);
var __expect1 = NaN;
var __result2 = Math.max(3, NaN);
var __expect2 = NaN;
var __result3 = Math.max(4, 6);
var __expect3 = 6;
var __result4 = Math.max(-2, 1);
var __expect4 = 1;
var __result5 = Math.max(__NumTop, 1);
var __expect5 = __NumTop;
var __result6 = Math.max(-1, __NumTop);
var __expect6 = __NumTop;
var __result7 = Math.max();
var __expect7 = -Infinity;
var __result8 = Math.max(1,2,3,NaN);
var __expect8 = NaN;
var __result9 = Math.max(3,6,9,1,3,4);
var __expect9 = 9;
