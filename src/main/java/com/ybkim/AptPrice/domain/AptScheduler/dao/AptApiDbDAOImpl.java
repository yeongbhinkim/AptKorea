package com.ybkim.AptPrice.domain.AptScheduler.dao;


import com.ybkim.AptPrice.domain.AptScheduler.AptApiDb;
import com.ybkim.AptPrice.domain.AptScheduler.CountyCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor

public class AptApiDbDAOImpl implements AptApiDbDAO {

    private final JdbcTemplate jdbcTemplate;

    /**
     * API -> DB insert
     *
     * @return
     */

    @Override
    public void insertApiDb(AptApiDb aptApiDb) {
//    System.out.println("aptApiDb = " + aptApiDb);
        StringBuilder sql = new StringBuilder();
        sql.append(" INSERT INTO apt ( ");
        sql.append("     apt_id, city, street, bon_bun, bu_bun, dan_gi_myeong, ");
        sql.append("     square_meter, contract_date, contract_day, amount, ");
        sql.append("     layer, construction_date, road_name, reason_cancellation_date, ");
        sql.append("     transaction_type, location_agency, full_contract_date ");
        sql.append(" ) VALUES ( ");
        sql.append("     NULL, ?, ?, ?, ?, ?, ");  // 'apt_id'는 AUTO_INCREMENT이므로 NULL을 사용합니다
        sql.append("     ?, ?, ?, ?, ");
        sql.append("     ?, ?, ?, ?, ");
        sql.append("     ?, ?, ? ");  // 'full_contract_date' 필드 추가
        sql.append(" ) ON DUPLICATE KEY UPDATE ");
        sql.append("     city = VALUES(city), street = VALUES(street), bon_bun = VALUES(bon_bun), ");
        sql.append("     bu_bun = VALUES(bu_bun), dan_gi_myeong = VALUES(dan_gi_myeong), ");
        sql.append("     square_meter = VALUES(square_meter), contract_date = VALUES(contract_date), ");
        sql.append("     contract_day = VALUES(contract_day), amount = VALUES(amount), ");
        sql.append("     layer = VALUES(layer), construction_date = VALUES(construction_date), ");
        sql.append("     road_name = VALUES(road_name), reason_cancellation_date = VALUES(reason_cancellation_date), ");
        sql.append("     transaction_type = VALUES(transaction_type), location_agency = VALUES(location_agency), ");
        sql.append("     full_contract_date = VALUES(full_contract_date)");


        jdbcTemplate.update(
                sql.toString(),
                aptApiDb.getCity(),
                aptApiDb.getStreet(),
                aptApiDb.getBon_bun(),
                aptApiDb.getBu_bun(),
                aptApiDb.getDan_gi_myeong(),
                aptApiDb.getSquare_meter(),
                aptApiDb.getContract_date(),
                aptApiDb.getContract_day(),
                aptApiDb.getAmount(),
                aptApiDb.getLayer(),
                aptApiDb.getConstruction_date(),
                aptApiDb.getRoad_name(),
                aptApiDb.getReason_cancellation_date(),
                aptApiDb.getTransaction_type(),
                aptApiDb.getLocation_agency(),
                aptApiDb.getFullContractDate() // 'FULL_CONTRACT_DATE' 필드를 위한 getter 추가
        );

    }


//  @Override
//  public void insertApiDb(AptApiDb  aptApiDb) {
//    StringBuffer sql = new StringBuffer();
//    System.out.println("aptApiDb = " + aptApiDb);
//    sql.append(" INSERT INTO apt ( ");
//    sql.append("     apt_id, city, street, bon_bun, bu_bun, dan_gi_myeong,  ");
//    sql.append("     square_meter, contract_date, contract_day, amount, ");
//    sql.append("     layer, construction_date, road_name, reason_cancellation_date, ");
//    sql.append("     transaction_type, location_agency ");
//    sql.append(" ) VALUES ( ");
//    sql.append("     NEXTVAL(apt_id_seq), ?, ?, ?, ?, ?, ");
//    sql.append("     ?, ?, ?, ?, ");
//    sql.append("     ?, ?, ?, ?, ");
//    sql.append("     ?, ? ");
//    sql.append(" ) ON DUPLICATE KEY UPDATE ");
//    sql.append("     city = VALUES(city), street = VALUES(street), bon_bun = VALUES(bon_bun), ");
//    sql.append("     bu_bun = VALUES(bu_bun), dan_gi_myeong = VALUES(dan_gi_myeong), ");
//    sql.append("     square_meter = VALUES(square_meter), contract_date = VALUES(contract_date), ");
//    sql.append("     contract_day = VALUES(contract_day), amount = VALUES(amount), ");
//    sql.append("     layer = VALUES(layer), construction_date = VALUES(construction_date), ");
//    sql.append("     road_name = VALUES(road_name), reason_cancellation_date = VALUES(reason_cancellation_date), ");
//    sql.append("     transaction_type = VALUES(transaction_type), location_agency = VALUES(location_agency);");
//
//
//
//
////    sql.append(" INSERT INTO apt ( ");
////    sql.append("     apt_id, city, street, bon_bun, bu_bun, dan_gi_myeong,  ");
////    sql.append("     square_meter, contract_date, contract_day, amount, ");
////    sql.append("     layer, construction_date, road_name, reason_cancellation_date, ");
////    sql.append("     transaction_type, location_agency ");
////    sql.append(" ) VALUES ( ");
////    sql.append("     NEXT VALUE FOR apt_id_seq, ?, ?, ?, ?, ?, ");
////    sql.append("     ?, ?, ?, ?, ");
////    sql.append("     ?, ?, ?, ?, ");
////    sql.append("     ?, ? ");
////    sql.append(" ) ");
//
//    //배치 처리 : 여러건의 갱신작업을 한꺼번에 처리하므로 단건처리할때보다 성능이 좋다.
//    jdbcTemplate.batchUpdate(sql.toString(), new BatchPreparedStatementSetter() {
//      @Override
//      public void setValues(PreparedStatement ps, int i) throws SQLException {
//        ps.setString(1, aptApiDb.getCity());
//        ps.setString(2, aptApiDb.getStreet());
//        ps.setString(3, aptApiDb.getBon_bun());
//        ps.setString(4, aptApiDb.getBu_bun());
//        ps.setString(5, aptApiDb.getDan_gi_myeong());
//        ps.setString(6, aptApiDb.getSquare_meter());
//        ps.setString(7, aptApiDb.getContract_date());
//        ps.setString(8, aptApiDb.getContract_day());
//        ps.setString(9, aptApiDb.getAmount());
//        ps.setString(10, aptApiDb.getLayer());
//        ps.setString(11, aptApiDb.getConstruction_date());
//        ps.setString(12, aptApiDb.getRoad_name());
//        ps.setString(13, aptApiDb.getReason_cancellation_date());
//        ps.setString(14, aptApiDb.getTransaction_type());
//        ps.setString(15, aptApiDb.getLocation_agency());
//      }
//
//      //배치처리할 건수
//      @Override
//      public int getBatchSize() {
////        return aptApiDb.size();
//        return 1;
//      }
//
//    });
//
//  }

    /**
     * 시군구 조회
     *
     * @return
     */
    @Override
    public List<CountyCode> apiSelectRegionCounty() {
        StringBuffer sql = new StringBuffer();

        sql.append(" SELECT COUNTY_CODE FROM REGION_COUNTY ");

        List<CountyCode> list = jdbcTemplate.query(sql.toString(),
                new BeanPropertyRowMapper<>(CountyCode.class)
        );
        return list;
    }

    /**
     * 시군구 조회
     *
     * @return
     */
    @Override
    public CountyCode selectCity(String SCHEDULER_CITY_CODE) {
        StringBuffer sql = new StringBuffer();
        String cityCodePrefix = SCHEDULER_CITY_CODE.substring(0, 2);

        sql.append(" SELECT ");
        sql.append(" CONCAT((SELECT CITY_NM FROM region_city WHERE CITY_CODE = ?) ");
        sql.append(" ,' ', ");
        sql.append(" (SELECT COUNTY_NM FROM region_county WHERE COUNTY_CODE = ?)) AS SCHEDULER_CITY_CODE ");

        CountyCode city = jdbcTemplate.queryForObject(sql.toString(),
                new BeanPropertyRowMapper<>(CountyCode.class), cityCodePrefix, SCHEDULER_CITY_CODE
        );
        return city;
    }

}