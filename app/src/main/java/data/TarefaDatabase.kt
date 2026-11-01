package BlocoDeNotas.trabalhofinaldesenvolvimentoandroidintermediario.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {

    override fun migrate(
        database: SupportSQLiteDatabase
    ) {
        database.execSQL(
            "ALTER TABLE tarefas ADD COLUMN cor TEXT NOT NULL DEFAULT 'LARANJA'"
        )
    }
}

@Database(
    entities = [Tarefa::class],
    version = 2,
    exportSchema = false
)
abstract class TarefaDatabase : RoomDatabase() {

    abstract fun tarefaDao(): TarefaDao

    companion object {

        @Volatile
        private var INSTANCE: TarefaDatabase? = null

        fun obterBanco(context: Context): TarefaDatabase {

            return INSTANCE ?: synchronized(this) {

                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    TarefaDatabase::class.java,
                    "tarefas_db"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                    .also {
                        INSTANCE = it
                    }
            }
        }
    }
}