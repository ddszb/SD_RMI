#!/bin/bash
javac ClienteServidor.java
javac Cliente.java
javac ServidorCP.java
javac ServidorSP.java
rmic Servidor
