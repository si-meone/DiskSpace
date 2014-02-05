DiskSpace
=========

Under development

finding ways to get stuff natively in each OS

Linux & MAC
 
  du -d1 .  
  comma delimit: sudo du -d 1 / | tr "\\t" "," > output_mac_csv.txt

Windows XP upward
get this first for du
http://technet.microsoft.com/en-us/sysinternals/bb896651

 du -c -q -l 1 c:\
 comma delimit: du -c -q -l 1 c:\ /accepteula > output_windows_csv.txt

all comes down to first level both show sizes on the left mainly in bytes and then name of folder.
Seems they don't show file sizes so will need to some calculations.
On the windows one the list is in KiloBytes but the totals are Bytes.
On Linux all bytes (you can obviously add -h ...etc)
From history it's easier dealing with bytes.

Now in scala you can run commands 

`scala> import sys.process._` 
 
` scala> val result = "ls -al" !`  
` total 64`  
` drwxr-xr-x  10 Al  staff   340 May 18 18:00 .`  
` drwxr-xr-x   3 Al  staff   102 Apr  4 17:58 ..`  
` -rw-r--r--   1 Al  staff   118 May 17 08:34 Foo.sh`  
` -rw-r--r--   1 Al  staff  2727 May 17 08:34 Foo.sh.jar`  
` result: Int = 0`  

` scala> println(result)` 


Sysinternals accept EULA dialog, but /accepteula always works for me.

That leaves disk useage:
=========================
on windows 
fsutil volume diskfree c:

on linux 
df -h
