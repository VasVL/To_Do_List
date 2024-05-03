package com.example.to_dolist.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.to_dolist.db.entity.ToDoItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoItemsDao {
    // todo тут надо посмотреть создаётся ли сам id или нет
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg toDoItem: ToDoItemEntity)

    @Delete
    suspend fun delete(vararg toDoItem: ToDoItemEntity)

    @Update
    suspend fun update(vararg toDoItem: ToDoItemEntity)

    @Query("SELECT * FROM deals")
    fun getAll(): Flow<List<ToDoItemEntity>>
}