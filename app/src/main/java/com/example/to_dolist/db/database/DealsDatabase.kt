package com.example.to_dolist.db.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.to_dolist.db.dao.ToDoItemsDao
import com.example.to_dolist.db.entity.ToDoItemEntity
import com.example.to_dolist.db.typeConverter.DateConverter

@Database(entities = [ToDoItemEntity::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class DealsDatabase : RoomDatabase() {
    abstract fun todoItemDao(): ToDoItemsDao

}