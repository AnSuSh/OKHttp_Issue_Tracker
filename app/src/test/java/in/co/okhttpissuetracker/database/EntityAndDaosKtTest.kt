package `in`.co.okhttpissuetracker.database

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class EntityAndDaosKtTest{
    val str = "2021-07-14T01:57:09Z"
    val finalStr = "07-14-2021"

    private fun String.formatToDate(): String{
        val year = this.substring(0, 4)
        val month = this.substring(5, 7)
        val day = this.substring(8, 10)
        return "$month-$day-$year"
    }

    @Test
    fun `provided string is converted to date format - MM-DD-YYYY`(){
        assertThat(str.formatToDate()).isEqualTo(finalStr)
    }
}