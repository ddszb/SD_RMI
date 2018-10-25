start rmiregistry
start java ServidorCP
timeout 5
start java Cliente 1 acoes1.txt
start java Cliente 2 acoes2.txt
start java Cliente 3 acoes3.txt
pause