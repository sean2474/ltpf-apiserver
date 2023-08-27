package com.proj.ltpfapiserver.mapper;

import com.proj.ltpfapiserver.model.Item;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ItemMapper {

    // Create (Insert) an item
    @Insert("INSERT INTO User.Item (submitterId, title, description, category, extraInfo) VALUES (#{submitterId}, #{title}, #{description}, #{category}, #{extraInfo})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertItem(Item item);

    // Read (Select) all items
    @Select("SELECT * FROM User.Item")
    List<Item> findAll();

    // Read (Select) an item by its ID
    @Select("SELECT * FROM User.Item WHERE id = #{id}")
    Item findById(@Param("id") int id);

    // Update an item by its ID
    @Update("UPDATE User.Item SET submitterId = #{submitterId}, title = #{title}, description = #{description}, category = #{category}, extraInfo = #{extraInfo} WHERE id = #{id}")
    void updateItem(Item item);

    // Delete an item by its ID
    @Delete("DELETE FROM User.Item WHERE id = #{id}")
    void deleteItem(@Param("id") int id);
}