#!/bin/bash
for i in $(find /Thesis/Downloads -type f | grep 'simo.[0-9].*.zip');
do
[ -f "$i" ] && rm "$i"
done
for i in $(find /Thesis/Downloads -type d | grep 'simo.[0-9].*');
do
[ -d "$i" ] && zip -rj "$i.zip" "$i" && rm -r "$i"
done