import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.Call

// Define the API interface
interface FlaskApiService{
    @GET("/test")
    fun getData(): Call<ApiResponse>
}

// Define the data class to handle the response
data class ApiResponse(val message: String)

// Create the Retrofit client
object RetrofitClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:5000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: FlaskApiService = retrofit.create(FlaskApiService::class.java)
}