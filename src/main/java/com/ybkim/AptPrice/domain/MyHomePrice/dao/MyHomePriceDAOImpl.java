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
//    log.info("myHomePriceFilterCondition = {}", myHomePriceFilterCondition);
//    sql.append(" SELECT B.* ");
//    sql.append("  ,GET_CONTRACT(B.CITY,B.STREET,B.BON_BUN,B.BU_BUN,B.DAN_GI_MYEONG,B.SQUARE_METER,B.LAYER,B.CONSTRUCTION_DATE) AS CONTRACT  ");
//    sql.append("  ,GET_TRANSACTIONCOUNT_LIST(B.CITY,B.STREET,B.BON_BUN,B.BU_BUN,B.DAN_GI_MYEONG,B.SQUARE_METER,B.LAYER,B.CONSTRUCTION_DATE) AS TRANSACTIONCOUNTLIST ");
//    sql.append(" FROM (");
//    sql.append(" SELECT A.* ");
//    sql.append("  ,ROW_NUMBER() OVER (ORDER BY CONTRACT_DATE DESC, LPAD(CONTRACT_DAY, 2, '0') DESC) NO ");
//    sql.append("  FROM( ");
//    sql.append("   SELECT  ");
//    sql.append("  ROW_NUMBER() OVER (PARTITION BY A.CITY ,A.STREET ,A.BON_BUN ,A.BU_BUN ,A.DAN_GI_MYEONG ,A.SQUARE_METER ,A.LAYER ,A.CONSTRUCTION_DATE ORDER BY A.CONTRACT_DATE DESC) AS RN  ");
//    sql.append("  ,A.* FROM APT A ");
//    sql.append("   WHERE 1 = 1");
//    sql.append("   AND CONCAT(CONTRACT_DATE, LPAD(CONTRACT_DAY, 2, 0)) BETWEEN REPLACE(?,'-','') AND REPLACE(?,'-','') ");
//    sql.append("   AND CITY LIKE CONCAT('%', REPLACE(?, '전체', ''), '%', '%', REPLACE(?, '전체', ''), '%', '%', REPLACE(?, '전체', ''), '%') ");
//    sql.append("   AND CAST(SUBSTR(SQUARE_METER, 1, INSTR(SQUARE_METER, '.') - 1) AS UNSIGNED)   BETWEEN  CAST(? AS UNSIGNED)  AND  CAST(? AS UNSIGNED) ");
//    sql.append("   AND CAST(REPLACE(AMOUNT, ',', '') AS UNSIGNED) BETWEEN  CAST(REPLACE(?, ',', '') AS UNSIGNED)  AND  CAST(REPLACE(?, ',', '') AS UNSIGNED) ");
//    sql.append("   ORDER BY CONTRACT_DATE DESC, LPAD(CONTRACT_DAY, 2, '0') DESC ");
//    sql.append(" ) A ");
//    sql.append(" WHERE A.RN= 1 ");
//    sql.append(" ) B ");
//    sql.append(" WHERE B.NO BETWEEN CAST(? AS UNSIGNED) AND CAST(? AS UNSIGNED) ");




  sql.append(" SELECT B.*  ");
  sql.append("       ,GET_TRANSACTIONCOUNT_LIST(B.CITY,B.STREET,B.BON_BUN,B.BU_BUN,B.DAN_GI_MYEONG,B.SQUARE_METER,B.LAYER,B.CONSTRUCTION_DATE) AS TRANSACTIONCOUNTLIST  ");
  sql.append("     FROM (  ");
  sql.append("             SELECT A.* ,ROW_NUMBER() OVER (ORDER BY FULL_CONTRACT_DATE DESC) NO  ");
  sql.append("             FROM(  ");
  sql.append("                     SELECT  ");
  sql.append("                     ROW_NUMBER() OVER (PARTITION BY A.CITY ,A.STREET ,A.BON_BUN ,A.BU_BUN ,A.DAN_GI_MYEONG ,A.SQUARE_METER ,A.LAYER ORDER BY A.FULL_CONTRACT_DATE DESC) AS RN  ");
  sql.append("                     ,A.*  ");
  sql.append("                             FROM APT A  ");
  sql.append("                     WHERE 1 = 1  ");
  sql.append("                     AND FULL_CONTRACT_DATE BETWEEN ? AND ?  ");
  sql.append("                     AND CITY LIKE CONCAT('%', ?, '%', ?, '%', ?, '%')  ");
  sql.append("                     AND SQUARE_METER BETWEEN ? AND ?  ");
  sql.append("                     AND AMOUNT BETWEEN ? AND ?  ");
  sql.append("   ORDER BY A.FULL_CONTRACT_DATE DESC ");
  sql.append("     ) A  ");
  sql.append("     WHERE A.RN= 1  ");
  sql.append(" ) B  ");
  sql.append("     WHERE B.NO BETWEEN ? AND ?  ");
  sql.append("     ORDER BY CITY, LAYER  ");





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
    sql.append("   FROM APT A ");
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

    sql.append(" SELECT * FROM APT ");
    sql.append(" WHERE APT_ID = ? ");

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
    sql.append(" FROM APT A, (SELECT * ");
    sql.append("              FROM APT B ");
    sql.append("              WHERE APT_ID = ? ) B ");
    sql.append(" WHERE 1 = 1 ");
    sql.append(" AND A.CITY = B.CITY  ");
    sql.append(" AND A.STREET = B.STREET  ");
    sql.append(" AND A.BON_BUN = B.BON_BUN  ");
    sql.append(" AND A.BU_BUN = B.BU_BUN  ");
    sql.append(" AND A.DAN_GI_MYEONG = B.DAN_GI_MYEONG  ");
    sql.append(" AND A.SQUARE_METER = B.SQUARE_METER  ");
    sql.append(" AND A.LAYER = B.LAYER  ");
    sql.append(" AND A.CONSTRUCTION_DATE = B.CONSTRUCTION_DATE  ");
    sql.append(" ORDER BY FULL_CONTRACT_DATE DESC ");

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
    sql.append(" SELECT REPLACE(A.FULL_CONTRACT_DATE,'-','') as x, A.AMOUNT as y ");
    sql.append(" FROM APT A, (SELECT * ");
    sql.append("              FROM APT B ");
    sql.append("              WHERE APT_ID = ? ) B ");
    sql.append(" WHERE 1 = 1 ");
    sql.append(" AND A.CITY = B.CITY ");
    sql.append(" AND A.STREET = B.STREET ");
    sql.append(" AND A.BON_BUN = B.BON_BUN ");
    sql.append(" AND A.BU_BUN = B.BU_BUN ");
    sql.append(" AND A.DAN_GI_MYEONG = B.DAN_GI_MYEONG ");
    sql.append(" AND A.SQUARE_METER = B.SQUARE_METER ");
    sql.append(" AND A.LAYER = B.LAYER ");
    sql.append(" AND A.CONSTRUCTION_DATE = B.CONSTRUCTION_DATE ");

    List<MyHomePrice> ScatterChart = jdbcTemplate.query(sql.toString(),
        new BeanPropertyRowMapper<>(MyHomePrice.class),
        apt_id
    );

//    log.info("ScatterChart = {}", ScatterChart);

    return ScatterChart;
  }


}