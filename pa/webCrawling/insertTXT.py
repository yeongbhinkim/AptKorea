import pymysql
import configparser
import chardet
import logging

# 로그 설정
logging.basicConfig(level=logging.DEBUG)  # 로그 레벨을 DEBUG로 설정

# 설정 파일 읽기
config = configparser.ConfigParser()
config.read('config.ini')

# 데이터베이스 연결 설정
db_config = config['Database']
connection = pymysql.connect(host=db_config['host'],
                             port=int(db_config['port']),
                             user=db_config['db_user'],
                             password=db_config['db_pass'],
                             database=db_config['db_name'],
                             charset='utf8mb4',
                             cursorclass=pymysql.cursors.DictCursor)

def insert_into_database(code, full_name):
    name_parts = full_name.split()
    district_name = ''    
    logging.debug(f'name_parts1: {name_parts}')  # 로그로 district_name 값을 출력

    if len(name_parts) > 2 and name_parts[-1] != '존재':
        district_name = ' '.join(name_parts[2:])
        logging.debug(f'District Name4: {district_name}')  # 로그로 district_name 값을 출력
    elif len(name_parts) > 1 and name_parts[-1] != '존재':
        district_name = ' '.join(name_parts[1:-1])
        logging.debug(f'District Name5: {district_name}')  # 로그로 district_name 값을 출력



    with connection.cursor() as cursor:
        if len(name_parts) == 2:
            # region_county 테이블에 삽입
            sql = "INSERT INTO region_county (COUNTY_CODE, COUNTY_NM) VALUES (%s, %s)"
            cursor.execute(sql, (code, name_parts[1]))
        elif district_name:
            # region_districts 테이블에 삽입
            sql = "INSERT INTO region_districts (DISTRICTS_CODE, DISTRICTS_NM) VALUES (%s, %s)"
            cursor.execute(sql, (code, district_name))
        else:
            # region_city 테이블에 삽입
            sql = "INSERT INTO region_city (CITY_CODE, CITY_NM) VALUES (%s, %s)"
            cursor.execute(sql, (code, name_parts[0]))
    connection.commit()

# 파일 인코딩 탐지
def detect_encoding(file_path):
    with open(file_path, 'rb') as file:
        raw_data = file.read()
    return chardet.detect(raw_data)['encoding']

# 사용할 인코딩 탐지
txt_folder_path = config['TXT']['folder_path']
txt_file_path = txt_folder_path + '\\법정동코드 전체자료.txt'
file_encoding = detect_encoding(txt_file_path)

# TXT 파일 처리
try:
    with open(txt_file_path, 'r', encoding=file_encoding) as file:
        for line in file:
            parts = line.strip().split('\t')
            if parts[-1] != '존재':
                continue  # '존재' 상태가 아니면 무시

            code, full_name = parts[0], parts[1]
            insert_into_database(code, full_name)
finally:
    connection.close()
