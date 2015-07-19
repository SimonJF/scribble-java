#!/usr/bin/env bash
rm compiled/*
mvn clean install
find . -name \*.jar -exec cp {} compiled \;
