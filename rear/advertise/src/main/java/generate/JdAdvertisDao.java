package generate;

import generate.JdAdvertis;

public interface JdAdvertisDao {
    int deleteByPrimaryKey(Long jdAdId);

    int insert(JdAdvertis record);

    int insertSelective(JdAdvertis record);

    JdAdvertis selectByPrimaryKey(Long jdAdId);

    int updateByPrimaryKeySelective(JdAdvertis record);

    int updateByPrimaryKey(JdAdvertis record);
}