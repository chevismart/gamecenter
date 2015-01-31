package gamecenter.core.dao;

import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by Chevis on 2015/1/27.
 */
public interface AccessChannelDao {
    @Select("select channelId from access_channel")
    public List<String> selectAll();

    @Select("select channelId from access_channel where channelId = #{channelId}")
    public String selectAccessChannel(String channelId);
}
