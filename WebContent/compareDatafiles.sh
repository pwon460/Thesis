#!/bin/bash
BASEDIR=$(dirname $0)
echo $BASEDIR
if [ ! -d "$BASEDIR/Downloads/initial" ]; then
	mkdir -p "$BASEDIR/Downloads/initial"
fi
tempPath="$BASEDIR/Downloads/initial/temp"
mkdir "$tempPath"
dirPath="$BASEDIR/Downloads/initial/new"
mkdir "$dirPath"
sqlite3 $BASEDIR/server2.db <<!
.headers off
.mode csv
.output $tempPath/bus_stops.txt
select * from STOPS;
.output $dirPath/bus_routes_orders.txt
select * from bus_routes_orders;
.output $tempPath/bus_routes_names.txt
select * from (
select distinct d.routeId, s.lineName, p.description
from bus_calendar as d
join bus_patterns as p on d.journeyPatternSectionId = p.journeyPatternSectionRef
join bus_sections as s on p.journeyPatternSectionRef = s.journeyPatternSectionId
order by s.lineName)
order by cast (lineName as integer);
.output $dirPath/bus_calendar.txt
select privateCode, dayId, routeId, journeyPatternSectionId, departureTime from bus_calendar;
.output $dirPath/bus_trips.txt
select journeyPatternSectionId, seq, arrival from bus_trips order by journeyPatternSectionId, seq;
.output $tempPath/rail_stations.txt
select * from rail_stations;
.output $dirPath/rail_routes_orders.txt
select * from rail_routes_orders;
.output $tempPath/rail_routes_names.txt
select distinct d.routeId, s.lineName, p.description
from rail_calendar as d
join rail_patterns as p on d.journeyPatternSectionId = p.journeyPatternSectionRef
join rail_sections as s on p.journeyPatternSectionRef = s.journeyPatternSectionId
order by cast (s.lineName as integer);
.output $dirPath/rail_calendar.txt
select privateCode, dayId, routeId, journeyPatternSectionId, departureTime from rail_calendar;
.output $dirPath/rail_trips.txt
select journeyPatternSectionId, seq, arrival from rail_trips order by journeyPatternSectionId, seq;
.output $tempPath/ferry_wharfs.txt
select * from wharfs;
.output $dirPath/ferry_routes_orders.txt
select * from ferry_routes_orders;
.output $tempPath/ferry_routes_names.txt
select distinct d.routeId, s.lineName, p.description
from ferry_calendar as d
join ferry_patterns as p on d.journeyPatternSectionId = p.journeyPatternSectionRef
join ferry_sections as s on p.journeyPatternSectionRef = s.journeyPatternSectionId
order by cast (s.lineName as integer);
.output $dirPath/ferry_calendar.txt
select privateCode, dayId, routeId, journeyPatternSectionId, departureTime from ferry_calendar;
.output $dirPath/ferry_trips.txt
select journeyPatternSectionId, seq, arrival from ferry_trips order by journeyPatternSectionId, seq;
.output $tempPath/light_rail_stations.txt
select * from light_rail_stations;
.output $dirPath/light_rail_routes_orders.txt
select * from light_rail_routes_orders;
.output $tempPath/light_rail_routes_names.txt
select distinct d.routeId, s.lineName, p.description
from light_rail_calendar as d
join light_rail_patterns as p on d.journeyPatternSectionId = p.journeyPatternSectionRef
join light_rail_sections as s on p.journeyPatternSectionRef = s.journeyPatternSectionId
order by cast (s.lineName as integer);
.output $dirPath/light_rail_calendar.txt
select privateCode, dayId, routeId, journeyPatternSectionId, departureTime from light_rail_calendar;
.output $dirPath/light_rail_trips.txt
select journeyPatternSectionId, seq, arrival from light_rail_trips order by journeyPatternSectionId, seq;
.output $dirPath/days_variation.txt
select * from days_variation;
!
sed 's/"//g' "$tempPath/bus_stops.txt" > "$dirPath/bus_stops.txt"
sed 's/"//g' "$tempPath/rail_stations.txt" > "$dirPath/rail_stations.txt"
sed 's/"//g' "$tempPath/ferry_wharfs.txt" > "$dirPath/ferry_wharfs.txt"
sed 's/"//g' "$tempPath/light_rail_stations.txt" > "$dirPath/light_rail_stations.txt"
sed 's/"//g' "$tempPath/bus_routes_names.txt" > "$dirPath/bus_routes_names.txt"
sed 's/"//g' "$tempPath/rail_routes_names.txt" > "$dirPath/rail_routes_names.txt"
sed 's/"//g' "$tempPath/ferry_routes_names.txt" > "$dirPath/ferry_routes_names.txt"
sed 's/"//g' "$tempPath/light_rail_routes_names.txt" > "$dirPath/light_rail_routes_names.txt"
if [ -d "$tempfDir" ]; then
	rm -r "$tempDir"
fi
currDir="$BASEDIR/Downloads/initial/curr"
diffDir="$BASEDIR/Downloads/initial/change"
mkdir -p "$currDir"
if [ -d "$diffDir" ]; then
	rm -r "$diffDir"
fi
mkdir "$diffDir"
for i in "$currDir"/*
do
#echo "$(basename "$i")"
filename="$(basename "$i")"
#echo "$dirPath/$filename"
diff --changed-group-format="%<" --unchanged-group-format="" "$i" "$dirPath/$filename" > "$diffDir/d_$filename.txt"
diff --changed-group-format="%<" --unchanged-group-format="" "$dirPath/$filename" "$i" > "$diffDir/i_$filename.txt"
done
rm -r $currDir
mv $dirPath $currDir
zipFilePath="$BASEDIR/Downloads"
zip -rj "$zipFilePath/simo.init.zip" "$currDir"
now=$(date +"%Y%m%d")
zip -rj "$zipFilePath/simo.patch.$now.zip" "$diffDir"