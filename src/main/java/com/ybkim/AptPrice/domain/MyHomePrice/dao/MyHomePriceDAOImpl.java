package com.ybkim.AptPrice.domain.MyHomePrice.dao;


import com.ybkim.AptPrice.domain.MyHomePrice.MyHomePrice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor

public class MyHomePriceDAOImpl implements MyHomePriceDAO {

  private final JdbcTemplate jdbcTemplate;

  /**
   * APT 조건조회
   *
   * @param myHomePriceFilterCondition
   * @return
   */
  @Override
  public List<MyHomePrice> selectMyHomePriceList(MyHomePriceFilterCondition myHomePriceFilterCondition) {
    StringBuffer sql = new StringBuffer();


  sql.append(" SELECT B.*  ");
  sql.append("       ,GET_TRANSACTIONCOUNT_LIST(B.city,B.street,B.bon_bun,B.bu_bun,B.dan_gi_myeong,B.square_meter,B.layer,B.construction_date) AS TRANSACTIONCOUNTLIST  ");
  sql.append("     FROM (  ");
  sql.append("             SELECT A.* ,ROW_NUMBER() OVER (ORDER BY full_contract_date DESC) NO  ");
  sql.append("             FROM(  ");
  sql.append("                     SELECT  ");
  sql.append("                     ROW_NUMBER() OVER (PARTITION BY A.city ,A.street ,A.bon_bun ,A.bu_bun ,A.dan_gi_myeong ,A.square_meter ,A.LAYER order BY A.full_contract_date DESC) AS RN  ");
  sql.append("                     ,A.*  ");
  sql.append("                             FROM apt A  ");
  sql.append("                     WHERE 1 = 1  ");
  sql.append("                     AND full_contract_date BETWEEN ? AND ?  ");
  sql.append("                     AND city LIKE CONCAT('%', ?, '%', ?, '%', ?, '%')  ");
  sql.append("                     AND square_meter BETWEEN ? AND ?  ");
  sql.append("                     AND amount BETWEEN ? AND ?  ");
  sql.append("   ORDER BY A.full_contract_date DESC ");
  sql.append("     ) A  ");
  sql.append("     WHERE A.RN= 1  ");
  sql.append(" ) B  ");
  sql.append("     WHERE B.NO BETWEEN ? AND ?  ");
  sql.append("     ORDER BY city, layer  ");





    List<MyHomePrice> list = jdbcTemplate.query(sql.toString(),
        new BeanPropertyRowMapper<>(MyHomePrice.class),
        myHomePriceFilterCondition.getContractDate(),
        myHomePriceFilterCondition.getContractDateTo(),
        myHomePriceFilterCondition.getSearchSidoCd(),
        myHomePriceFilterCondition.getSearchGugunCd(),
        myHomePriceFilterCondition.getSearchDongCd(),
//        myHomePriceFilterCondition.getSearchArea(),
        myHomePriceFilterCondition.getSearchAreaValue(),
        myHomePriceFilterCondition.getSearchAreaValueTo(),
        myHomePriceFilterCondition.getSearchFromAmount(),
        myHomePriceFilterCondition.getSearchToAmnount(),
        myHomePriceFilterCondition.getStartRec(),
        myHomePriceFilterCondition.getEndRec()
    );

//    log.info("list123 = {}", list);

    return list;
  }

  /**
   * 전체건수
   *
   * @return
   */
  @Override
  public int totalCount(MyHomePriceFilterCondition myHomePriceFilterCondition) {
    StringBuffer sql = new StringBuffer();
    log.info("myHomePriceFilterCondition123 = {}", myHomePriceFilterCondition);

    sql.append(" SELECT count(*) as COUNT FROM( ");
    sql.append("   SELECT ");
    sql.append("   ROW_NUMBER() OVER (PARTITION BY A.CITY ,A.STREET ,A.BON_BUN ,A.BU_BUN ,A.DAN_GI_MYEONG ,A.SQUARE_METER ,A.LAYER ,A.CONSTRUCTION_DATE ORDER BY A.CONTRACT_DATE DESC) AS RN  ");
    sql.append("   FROM apt A ");
    sql.append("   WHERE 1 = 1  ");
    sql.append("   AND FULL_CONTRACT_DATE BETWEEN ? AND ?  ");
    sql.append("   AND CITY LIKE CONCAT('%', ?, '%', ?, '%', ?, '%')  ");
    sql.append("   AND SQUARE_METER  BETWEEN ? AND ?  ");
    sql.append("   AND AMOUNT BETWEEN ? AND ?  ");
    sql.append(" ) A ");
    sql.append(" WHERE A.RN= 1 ");

    Integer cnt = 0;
    cnt = jdbcTemplate.queryForObject(
        sql.toString(), Integer.class,
        myHomePriceFilterCondition.getContractDate(),
        myHomePriceFilterCondition.getContractDateTo(),
        myHomePriceFilterCondition.getSearchSidoCd(),
        myHomePriceFilterCondition.getSearchGugunCd(),
        myHomePriceFilterCondition.getSearchDongCd(),
        myHomePriceFilterCondition.getSearchAreaValue(),
        myHomePriceFilterCondition.getSearchAreaValueTo(),
        myHomePriceFilterCondition.getSearchFromAmount(),
        myHomePriceFilterCondition.getSearchToAmnount()
        );
//    log.info("cnt1 = {}", cnt);

    return cnt;
  }

  /**
   * APT 상세조회 폼
   *
   * @param apt_id
   * @return
   */
  @Override
  public MyHomePrice selectMyHomePriceDetailForm(Long apt_id) {

    StringBuffer sql = new StringBuffer();

    sql.append(" SELECT * FROM apt ");
    sql.append(" WHERE apt_id = ? ");

//    MyHomePrice MyHomePriceItem = null;
//    try {
//      MyHomePriceItem = jdbcTemplate.queryForObject(
//          sql.toString(),
//          new BeanPropertyRowMapper<>(MyHomePrice.class),
//          myHomePriceFilterCondition.getApt_id()
//      );
//    } catch (Exception e) { //1건을 못찾으면
//      MyHomePriceItem = null;
//    }

    MyHomePrice MyHomePriceItem = jdbcTemplate.queryForObject(
        sql.toString(),
        new BeanPropertyRowMapper<>(MyHomePrice.class),
        apt_id
    );

//    log.info("MyHomePriceItem = {}", MyHomePriceItem);

    return MyHomePriceItem;
  }

  /**
   * APT 상세조회 리스트
   *
   * @param apt_id
   * @return
   */
  @Override
  public List<MyHomePrice> selectMyHomePriceDetail(Long apt_id) {
    StringBuffer sql = new StringBuffer();

    sql.append(" SELECT A.* ");
    sql.append(" FROM apt A, (SELECT * ");
    sql.append("              FROM apt B ");
    sql.append("              WHERE apt_id = ? ) B ");
    sql.append(" WHERE 1 = 1 ");
    sql.append(" AND A.city = B.city  ");
    sql.append(" AND A.street = B.street  ");
    sql.append(" AND A.bon_bun = B.bon_bun  ");
    sql.append(" AND A.bu_bun = B.bu_bun  ");
    sql.append(" AND A.dan_gi_myeong = B.dan_gi_myeong  ");
    sql.append(" AND A.square_meter = B.square_meter  ");
    sql.append(" AND A.layer = B.layer  ");
    sql.append(" AND A.construction_date = B.construction_date  ");
    sql.append(" ORDER BY full_contract_date DESC ");

    List<MyHomePrice> detaillist = jdbcTemplate.query(sql.toString(),
        new BeanPropertyRowMapper<>(MyHomePrice.class),
        apt_id
    );

//    log.info("detaillist = {}", detaillist);

    return detaillist;
  }

  /**
   * APT 상세조회 ScatterChart
   *
   * @param apt_id
   * @return
   */
  @Override
  public List<MyHomePrice> selectMyHomePriceScatterChart(Long apt_id) {
    StringBuffer sql = new StringBuffer();
//    sql.append(" SELECT CONCAT(A.CONTRACT_DATE, LPAD(A.CONTRACT_DAY, 2, '0')) as x, TRIM(REPLACE(A.AMOUNT, ',', '')) as y ");
    sql.append(" SELECT REPLACE(A.full_contract_date,'-','') as x, A.amount as y ");
    sql.append(" FROM apt A, (SELECT * ");
    sql.append("              FROM apt B ");
    sql.append("              WHERE apt_id = ? ) B ");
    sql.append(" WHERE 1 = 1 ");
    sql.append(" AND A.city = B.city ");
    sql.append(" AND A.street = B.street ");
    sql.append(" AND A.bon_bun = B.bon_bun ");
    sql.append(" AND A.bu_bun = B.bu_bun ");
    sql.append(" AND A.dan_gi_myeong = B.dan_gi_myeong ");
    sql.append(" AND A.square_meter = B.square_meter ");
    sql.append(" AND A.layer = B.layer ");
    sql.append(" AND A.construction_date = B.construction_date ");

    List<MyHomePrice> ScatterChart = jdbcTemplate.query(sql.toString(),
        new BeanPropertyRowMapper<>(MyHomePrice.class),
        apt_id
    );

//    log.info("ScatterChart = {}", ScatterChart);

    return ScatterChart;
  }


}