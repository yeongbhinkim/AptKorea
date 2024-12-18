package com.ybkim.AptPrice.domain.home.dao;


import com.ybkim.AptPrice.domain.home.home;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor

public class homeDAOImpl implements homeDAO {

  private final JdbcTemplate jdbcTemplate;

  /**
   * APT 조건조회
   *
   * @return
   */
  @Override
  public List<home> selectRegionCity() {
    StringBuffer sql = new StringBuffer();

    sql.append(" SELECT * FROM region_city ORDER BY 2 ");

    List<home> list = jdbcTemplate.query(sql.toString(),
        new BeanPropertyRowMapper<>(home.class)
    );
    return list;
  }

  /**
   * 시군구 조회
   *
   * @return
   */
  @Override
  public List<home> selectRegionCounty(String CITY_CODE) {
    StringBuffer sql = new StringBuffer();

    sql.append(" SELECT * FROM region_county  ");
    sql.append(" WHERE SUBSTR(county_code, 1,2 ) = ? ");
    sql.append(" ORDER BY 2 ");

    List<home> list = jdbcTemplate.query(sql.toString(),
        new BeanPropertyRowMapper<>(home.class),CITY_CODE
    );
    return list;
  }

  /**
   * 읍면동 조회
   *
   * @return
   */
  @Override
  public List<home> selectRegionDistricts(String COUNTY_CODE) {
    StringBuffer sql = new StringBuffer();

    sql.append(" SELECT * FROM region_districts  ");
    sql.append(" WHERE SUBSTR(districts_code, 1, 5 ) = ? ");
    sql.append(" ORDER BY 2 ");

    List<home> list = jdbcTemplate.query(sql.toString(),
        new BeanPropertyRowMapper<>(home.class),COUNTY_CODE
    );
    return list;
  }


}