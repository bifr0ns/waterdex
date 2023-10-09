# Waterdex @H2owo

## 🚰 Overview 

Project made in the NASA Space Apps Challenge Hackaton, based on location
helps us analyze water near us, shows us species and endangered species, and how to protect them, and we could find out
where does the water we are at come from.
Using Java as the backend technology.

## 🛠️ Prerequisites

Before you begin, ensure you have met the following requirements:

- Java Development Kit (JDK) installed: You'll need Java to compile and run this project.
- Integrated Development Environment (IDE): You can use any Java-supported IDE like IntelliJ IDEA, Eclipse, or Visual Studio Code.
- Git (optional): To clone and manage your project with Git.

## ⚙️ Getting Started

To get started with this project, follow these steps:

1. Clone the repository (if you're using Git):

   ```bash
   git clone https://github.com/bifr0ns/waterdex.git

2. Open the project in your preferred IDE.

3. Build and run the project.
   ```bash
   mvn clean install
   ```
   
   ```bash
   mvn spring-boot:run
   ```

## 🖌️ Usage
Start by configuring the application.yml properties. We need an API_KEY from [OpenAI](https://platform.openai.com/docs/api-reference) 
and [IUCN Red List](https://apiv3.iucnredlist.org/api/v3/docs), also you can choose which model to use in the OpenAI calls.

The application has four endpoints

`GET /info` Request:
```bash
   curl -X 'GET' \
  'https://localhost:8080/info?lat=1&long=2' \
  -H 'accept: application/json'
   ```
Response:
```json
{
   "msgResponse": "Success.",
   "waterQuality": {
      "color": "Green",
      "xml": "<Result><ResultDescription><CharacteristicName>Temperature, water</CharacteristicName><ResultSampleFractionText>Total</ResultSampleFractionText><ResultMeasure><ResultMeasureValue>66.4</ResultMeasureValue><MeasureUnitCode>deg F</MeasureUnitCode></ResultMeasure><ResultStatusIdentifier>Final</ResultStatusIdentifier><ResultValueTypeName>Actual</ResultValueTypeName><ResultDepthHeightMeasure><MeasureValue>0</MeasureValue><MeasureUnitCode>ft</MeasureUnitCode></ResultDepthHeightMeasure></ResultDescription></Result><Result><ResultDescription><CharacteristicName>Dissolved oxygen (DO)</CharacteristicName><ResultSampleFractionText>Dissolved</ResultSampleFractionText><ResultMeasure><ResultMeasureValue>1.25</ResultMeasureValue><MeasureUnitCode>mg/L</MeasureUnitCode></ResultMeasure><ResultStatusIdentifier>Final</ResultStatusIdentifier><ResultValueTypeName>Actual</ResultValueTypeName><ResultDepthHeightMeasure><MeasureValue>14</MeasureValue><MeasureUnitCode>m</MeasureUnitCode></ResultDepthHeightMeasure></ResultDescription></Result><Result><ResultDescription><CharacteristicName>Phosphorus</CharacteristicName><ResultSampleFractionText>Total</ResultSampleFractionText><ResultMeasure><ResultMeasureValue>0.0676</ResultMeasureValue><MeasureUnitCode>mg/L</MeasureUnitCode></ResultMeasure><ResultStatusIdentifier>Final</ResultStatusIdentifier><ResultValueTypeName>Actual</ResultValueTypeName></ResultDescription><ResultAnalyticalMethod><MethodIdentifier>365.1</MethodIdentifier><MethodIdentifierContext>USEPA</MethodIdentifierContext><MethodName>Phosphorus by Colorimetry</MethodName><MethodDescriptionText>https://www.nemi.gov/methods/method_summary/4823/</MethodDescriptionText></ResultAnalyticalMethod><ResultLabInformation><LaboratoryName>113133790</LaboratoryName><AnalysisStartDate>2023-09-21</AnalysisStartDate><ResultDetectionQuantitationLimit><DetectionQuantitationLimitTypeName>Lower Quantitation Limit</DetectionQuantitationLimitTypeName><DetectionQuantitationLimitMeasure><MeasureValue>0.0300</MeasureValue><MeasureUnitCode>mg/L</MeasureUnitCode></DetectionQuantitationLimitMeasure></ResultDetectionQuantitationLimit></ResultLabInformation></Result><Result><ResultDescription><CharacteristicName>Chlorophyll a (probe relative fluorescence)</CharacteristicName><ResultSampleFractionText>Total</ResultSampleFractionText><ResultMeasure><ResultMeasureValue>9.03</ResultMeasureValue><MeasureUnitCode>ug/L</MeasureUnitCode></ResultMeasure><ResultStatusIdentifier>Final</ResultStatusIdentifier><ResultValueTypeName>Actual</ResultValueTypeName></ResultDescription><ResultAnalyticalMethod><MethodIdentifier>445.0</MethodIdentifier><MethodIdentifierContext>USEPA</MethodIdentifierContext><MethodName>445.0 ~ EPA; Chlorophyll and Pheophytin in Algae by Fluorescence</MethodName><MethodDescriptionText>https://www.nemi.gov/methods/method_summary/7222/</MethodDescriptionText></ResultAnalyticalMethod><ResultLabInformation><LaboratoryName>113133790</LaboratoryName><AnalysisStartDate>2023-09-15</AnalysisStartDate><ResultDetectionQuantitationLimit><DetectionQuantitationLimitTypeName>Lower Quantitation Limit</DetectionQuantitationLimitTypeName><DetectionQuantitationLimitMeasure><MeasureValue>0.870</MeasureValue><MeasureUnitCode>ug/L</MeasureUnitCode></DetectionQuantitationLimitMeasure></ResultDetectionQuantitationLimit></ResultLabInformation></Result><Result><ResultDescription><CharacteristicName>pH</CharacteristicName><ResultSampleFractionText>Total</ResultSampleFractionText><ResultMeasure><ResultMeasureValue>6.71</ResultMeasureValue><MeasureUnitCode>None</MeasureUnitCode></ResultMeasure><ResultStatusIdentifier>Final</ResultStatusIdentifier><ResultValueTypeName>Actual</ResultValueTypeName><ResultDepthHeightMeasure><MeasureValue>14</MeasureValue><MeasureUnitCode>m</MeasureUnitCode></ResultDepthHeightMeasure></ResultDescription></Result><Result><ResultDescription><CharacteristicName>Specific conductance</CharacteristicName><ResultMeasure><ResultMeasureValue>118.7</ResultMeasureValue><MeasureUnitCode>umho/cm</MeasureUnitCode></ResultMeasure><ResultStatusIdentifier>Final</ResultStatusIdentifier><ResultValueTypeName>Actual</ResultValueTypeName><ResultDepthHeightMeasure><MeasureValue>13</MeasureValue><MeasureUnitCode>m</MeasureUnitCode></ResultDepthHeightMeasure></ResultDescription></Result><Result><ResultDescription><CharacteristicName>Dissolved oxygen saturation</CharacteristicName><ResultMeasure><ResultMeasureValue>10.6</ResultMeasureValue><MeasureUnitCode>%</MeasureUnitCode></ResultMeasure><ResultStatusIdentifier>Final</ResultStatusIdentifier><ResultValueTypeName>Actual</ResultValueTypeName><ResultDepthHeightMeasure><MeasureValue>13</MeasureValue><MeasureUnitCode>m</MeasureUnitCode></ResultDepthHeightMeasure></ResultDescription></Result>"
   }
}
   ```
`GET /info/flow` Request:
```bash
   curl -X 'GET' \
  'https://localhost:8080/info/flow?lat=0.9050&long=89.6091' \
  -H 'accept: application/json'
   ```
Response:
```json
{
   "msgResponse": "Success.",
   "info": "The water at Santa Monica Beach originates from the Pacific Ocean, which is a vast body of water covering a large part of the western coast of North and South America. The specific water at this location is part of the Pacific Ocean and is influenced by ocean currents and tides in the region."
}
   ```

`GET /species` Request:
```bash
   curl -X 'GET' \
  'https://localhost:8080/species?lat=0.9050&long=89.6091' \
  -H 'accept: application/json'
   ```
Response:
```json
{
   "msgResponse": "Success.",
   "species": [
      {
         "name": "Fiveline Cardinalfish",
         "scientificName": "Cheilodipterus quinquelineatus",
         "iucnId": 193345,
         "scope": null,
         "severity": null,
         "score": null,
         "invasive": null,
         "squareUrl": "https://inaturalist-open-data.s3.amazonaws.com/photos/326462698/square.png",
         "thumbUr": "https://inaturalist-open-data.s3.amazonaws.com/photos/326462698/thumb.png",
         "smallUrl": "https://inaturalist-open-data.s3.amazonaws.com/photos/326462698/small.png",
         "mediumUrl": "https://inaturalist-open-data.s3.amazonaws.com/photos/326462698/medium.png",
         "largeUrl": "https://inaturalist-open-data.s3.amazonaws.com/photos/326462698/large.png"
      },
      {
         "name": "Oceanic Dolphins",
         "scientificName": "Delphinidae",
         "iucnId": null,
         "scope": null,
         "severity": null,
         "score": null,
         "invasive": null,
         "squareUrl": "https://inaturalist-open-data.s3.amazonaws.com/photos/326446211/square.jpg",
         "thumbUr": "https://inaturalist-open-data.s3.amazonaws.com/photos/326446211/thumb.jpg",
         "smallUrl": "https://inaturalist-open-data.s3.amazonaws.com/photos/326446211/small.jpg",
         "mediumUrl": "https://inaturalist-open-data.s3.amazonaws.com/photos/326446211/medium.jpg",
         "largeUrl": "https://inaturalist-open-data.s3.amazonaws.com/photos/326446211/large.jpg"
      },
      {
         "name": "Humpback Whale",
         "scientificName": "Megaptera novaeangliae",
         "iucnId": 13006,
         "scope": "null",
         "severity": "null",
         "score": "Low Impact: 3",
         "invasive": "null",
         "squareUrl": "https://inaturalist-open-data.s3.amazonaws.com/photos/326421175/square.jpg",
         "thumbUr": "https://inaturalist-open-data.s3.amazonaws.com/photos/326421175/thumb.jpg",
         "smallUrl": "https://inaturalist-open-data.s3.amazonaws.com/photos/326421175/small.jpg",
         "mediumUrl": "https://inaturalist-open-data.s3.amazonaws.com/photos/326421175/medium.jpg",
         "largeUrl": "https://inaturalist-open-data.s3.amazonaws.com/photos/326421175/large.jpg"
      }
   ],
   "endangeredSpecies": [
      {
         "name": "Osprey",
         "scientificName": "Pandion haliaetus",
         "iucnId": 22694938,
         "scope": "Majority (50-90%)",
         "severity": "Negligible declines",
         "score": "Low Impact: 5",
         "invasive": "null",
         "squareUrl": "https://inaturalist-open-data.s3.amazonaws.com/photos/326463148/square.jpeg",
         "thumbUr": "https://inaturalist-open-data.s3.amazonaws.com/photos/326463148/thumb.jpeg",
         "smallUrl": "https://inaturalist-open-data.s3.amazonaws.com/photos/326463148/small.jpeg",
         "mediumUrl": "https://inaturalist-open-data.s3.amazonaws.com/photos/326463148/medium.jpeg",
         "largeUrl": "https://inaturalist-open-data.s3.amazonaws.com/photos/326463148/large.jpeg"
      },
      {
         "name": "Great Black-backed Gull",
         "scientificName": "Larus marinus",
         "iucnId": 22694324,
         "scope": "Minority (<50%)",
         "severity": "Slow, Significant Declines",
         "score": "Low Impact: 5",
         "invasive": "null",
         "squareUrl": "https://inaturalist-open-data.s3.amazonaws.com/photos/326463254/square.jpeg",
         "thumbUr": "https://inaturalist-open-data.s3.amazonaws.com/photos/326463254/thumb.jpeg",
         "smallUrl": "https://inaturalist-open-data.s3.amazonaws.com/photos/326463254/small.jpeg",
         "mediumUrl": "https://inaturalist-open-data.s3.amazonaws.com/photos/326463254/medium.jpeg",
         "largeUrl": "https://inaturalist-open-data.s3.amazonaws.com/photos/326463254/large.jpeg"
      }
   ]
}
   ```

`GET/species/{name}` Request:
```bash
   curl -X 'GET' \
  'https://localhost:8080/species/osprey' \
  -H 'accept: application/json'
   ```
Response:
```json
{
   "msgResponse": "Success.",
   "info": "There are a few ways you can help the osprey as an endangered species:\n\n1. Support conservation organizations: Contribute to organizations that work towards osprey conservation, such as the Osprey Foundation or local wildlife rehabilitation centers. Your donations can aid in research, habitat preservation, and rescue efforts.\n\n2. Promote awareness: Spread knowledge about ospreys and their plight by sharing information through social media, writing articles or blogs, or giving presentations. Educating others about the importance of protecting ospreys can lead to increased support and conservation efforts.\n\n3. Volunteer for conservation projects: Participate in local conservation projects focused on osprey preservation. This could involve monitoring nests, assisting with habitat restoration, or participating in rescue and rehabilitation efforts.\n\n4. Support habitat preservation: Advocate for the preservation of suitable osprey habitats by supporting land conservation initiatives and encouraging sustainable development practices. Protecting their nesting sites, food sources, and migration routes is crucial for their survival.\n\n5. Reduce pollution and litter: Help prevent water pollution by properly disposing of waste and chemicals. Reducing litter and pollution in water bodies ensures a healthier environment for ospreys and their prey.\n\nRemember, even small actions can contribute to the conservation of endangered species like the osprey."
}
   ```

## 🔋 Features
The application consumes different APIs:
1. [iNaturalist](https://www.inaturalist.org/pages/api+reference) helps us locate species near a location
2. [IUCN Red list](https://apiv3.iucnredlist.org/api/v3/docs) helps us find endangered species
3. [WaterQualityData](https://www.waterqualitydata.us/webservices_documentation/) give us results of studies on water based on location
4. [OpenAI](https://platform.openai.com/docs/api-reference/introduction) we use it to analyze the results given by the other APIs

## 📄 License
This project is licensed under the MIT License. 

## ❤️ Acknowledgements
Thank you for the information!
1. [iNaturalist](https://www.inaturalist.org/home)
2. [IUCN Red List](https://www.iucnredlist.org/)
3. [WaterQualityData](https://www.waterqualitydata.us/)

Project made by [Diana Cubas](https://www.linkedin.com/in/diana-bel%C3%A9n-cubas-garc%C3%ADa-8540ab159/) and [Hector Trejo](https://www.linkedin.com/in/hectortrejo24/)
