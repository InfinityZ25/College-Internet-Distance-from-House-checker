import org.json.JSONObject

class kotlinPart{
    val url = "https://www.att.com/services/shop/model/ecom/shop/view/unified/qualification/service/CheckAvailabilityRESTService/invokeCheckAvailability"
    val google_api_key = "AIzaSyAoh11FSJlApTyPcOGyfRiX_jV5Pnir-dQ"
    fun run(address: String){
        println("Attempting to reach ATT's database")
        //Input your data here
        val addressToBeChecked = address
        val zip = addressToBeChecked.split(", ")[1]
        //Program starts here
        val initialTime = System.nanoTime()
        val attRestRequest = khttp.get(url,
            params = mapOf("userInputZip" to zip, "userInputAddressLine1" to addressToBeChecked, "mode" to "fullAddress"))
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
                println("There is U-Verse ATT coverage available $addressToBeChecked" +
                        "\nMax available internet speed is: $highSpeed" +
                        "\nRequest took $requestTime seconds" +
                        "\nDistance from UCF is ${googleMapsInquiry.getJSONObject("distance").getString("text")} or ${googleMapsInquiry.getJSONObject("duration").getString("text")}")
            }else{
                println("No service available at this address"+
                        "\nDistance from UCF is ${googleMapsInquiry.getJSONObject("distance").getString("text")} or ${googleMapsInquiry.getJSONObject("duration").getString("text")}")
            }
        }else{
            println("Null request")
        }
    }

    fun gmapsQuery(from: String, to: String): JSONObject {
        val req = khttp.get(
            "https://maps.googleapis.com/maps/api/distancematrix/json?destinations=${to.replace(" ", "+")}" +
                    "&origins=${from.replace(" ", "+")}" +
                    "&units=imperial&sensor=false" +
                    "&key=$google_api_key"
        )

        return req.jsonObject
    }
}