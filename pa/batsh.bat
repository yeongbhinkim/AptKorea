@echo off
SETLOCAL ENABLEDELAYEDEXPANSION

SET folder_path=C:\
SET db_user=
SET db_pass=
SET db_name=
SET table_name=
SET mysql_path=C:\Program Files\MariaDB 11.2\bin\mysql

FOR %%G IN ("%folder_path%\*.csv") DO (
    SET "full_path=%%~fG"
    echo Importing !full_path! into the database...
    "%mysql_path%" -u %db_user% -p%db_pass% -P 3307 -e "LOAD DATA LOCAL INFILE '!full_path!' INTO TABLE %db_name%.%table_name% FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\\n' IGNORE 1 LINES;"
)

echo All files imported.
PAUSE
