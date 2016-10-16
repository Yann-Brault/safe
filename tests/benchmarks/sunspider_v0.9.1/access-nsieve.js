//function record(time) {
//    document.getElementById("console").innerHTML = time + "ms";
//    if (window.parent) {
//        parent.recordResult(time);
//    }
//}

//var _sunSpiderStartDate = new Date();

// The Great Computer Language Shootout
// http://shootout.alioth.debian.org/
//
// modified by Isaac Gouy

function pad(number,width){
   var s = number.toString();
   var prefixWidth = width - s.length;
   if (prefixWidth>0){
      for (var i=1; i<=prefixWidth; i++) s = " " + s;
   }
   return s;
}

function nsieve(m, isPrime){
   var i, k, count;

   for (i=2; i<=m; i++) { isPrime[i] = true; }
   count = 0;

   for (i=2; i<=m; i++){
      if (isPrime[i]) {
         for (k=i+i; k<=m; k+=i) isPrime[k] = false;
         count++;
      }
   }
   return count;
}

function sieve() {
    for (var i = 1; i <= 3; i++ ) {
        var m = (1<<i)*10000;
        var flags = Array(m+1);  // m is NumTop. Array() can cause a RangeError exception.
        nsieve(m, flags);
    }
}

sieve();


//var _sunSpiderInterval = new Date() - _sunSpiderStartDate;

//record(_sunSpiderInterval);
