import org.json.JSONObject

class kotlinPart{
    val url = "https://www.att.com/services/shop/model/ecom/shop/view/unified/qualification/service/CheckAvailabilityRESTService/invokeCheckAvailability"
    //This is my google api key. I Hope people don't steal it, whoops.
    //I will add a settings file later so that everyone can easily change their key without having to recompile
    val google_api_key = "AIzaSyAoh11FSJlApTyPcOGyfRiX_jV5Pnir-dQ"
    //This function gets called from Main in the java side of the program because I could not figure out a way to export a native Kotlin program
    fun run(address: String){
        //Just here to indicate where the function starts
        println("Attempting to reach ATT's database")
        //Input your data here. This line is kinda pointless and can be simplified but I won't bother to fix it just yet.
        val addressToBeChecked = address
        val zip = addressToBeChecked.split(", ")[1]
        //Program starts here, get the time to calcuate the difference later.
        val initialTime = System.nanoTime()
        //GET Request to AT&T's REST Service
        val attRestRequest = khttp.get(url,
            params = mapOf("userInputZip" to zip, "userInputAddressLine1" to addressToBeChecked, "mode" to "fullAddress"))
        //GET Request to Google Maps' Matrix Distance API.
        //UCF Can be changed for whatever college you want it to be.
        val googleRequest = gmapsQuery(addressToBeChecked, "UCF")
        //Check if the status of the response is ok
        if (attRestRequest.statusCode == 200){
            val hObject = attRestRequest.jsonObject
            //Get max available speed
            val highSpeed = hObject.getJSONObject("CkavDataBean").get("maxHsiaSpeedAvailable")
            //Asking GoogleMaps Distance Matrix API how far away in miles and minutes is it.
            val googleMapsInquiry = googleRequest.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0)
            //Test if high speed internet available in the address
            if(highSpeed is String){
                //Calculating how long does the request take to respond
                val requestTime = (System.nanoTime() - initialTime) / 1000000000
                //Send the user the data asked and calculated.
                println("There is U-Verse ATT coverage available $addressToBeChecked" +
                        "\nMax available internet speed is: $highSpeed" +
                        "\nRequest took $requestTime seconds" +
                        "\nDistance from UCF is ${googleMapsInquiry.getJSONObject("distance").getString("text")} or ${googleMapsInquiry.getJSONObject("duration").getString("text")}")
            }else{
                println("No service available at this address"+
                        "\nDistance from UCF is ${googleMapsInquiry.getJSONObject("distance").getString("text")} or ${googleMapsInquiry.getJSONObject("duration").getString("text")}")
            }
        }else{
            //I will handle this piece of the code better latter.
            println("Null request")
        }
    }
    //This is a recursive function to get the JSON data from Google Maps' Matrix Distance API.
    //The units argument can be set to metric, or be removed, if you wish.
    //Adding the argument sensor=false makes the request more lightweight and optimizes the usage of the api by a lot.
    fun gmapsQuery(from: String, to: String): JSONObject {
        return khttp.get(
            "https://maps.googleapis.com/maps/api/distancematrix/json?destinations=${to.replace(" ", "+")}" +
                    "&origins=${from.replace(" ", "+")}" +
                    "&units=imperial&sensor=false" +
                    "&key=$google_api_key"
        ).jsonObject
    }
}
