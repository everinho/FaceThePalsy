
# FaceThePalsy

Application supporting rehabilitation in facial nerve palsy using artificial intelligence models. The application allows you to assess paralysis and control rehabilitation exercises. It also controls the regularity of exercise. As AI I used Google API for face detection from ML Kit. User data are stored in local memory as json files. 


## Features

- Classification of the degree of facial nerve palsy by assessing facial asymmetry
- Selecting the intensity of rehabilitation exercises
- Supervising the correctness of exercises performed
- Progress control in the form of a training schedule


## Asymmetry classification

Facial nerve palsy is assessed based on facial asymmetry. There are three levels of infection. 
The following (31) points detected on the face are used for the classification process.
<img src="https://github.com/everinho/FaceThePalsy/assets/117845737/4fa42989-38e4-4111-abd9-df26518e82a8" alt="asymetria_1" width="400">
The following facial distances are then calculated.
<img src="https://github.com/everinho/FaceThePalsy/assets/117845737/34dca897-15d3-4dbf-b0cb-35ad7dea048c" alt="asymetria_2" width="400">
<img src="https://github.com/everinho/FaceThePalsy/assets/117845737/d420d5e3-e5a7-45a6-af6e-688401ead7c0" alt="asymetria_3" width="400">
<img src="https://github.com/everinho/FaceThePalsy/assets/117845737/60cc8f30-7f4d-4d9a-85d9-a72301023e86" alt="asymetria_4" width="400">
In order to minimize possible interference and errors in the measurement, 30 iterations are carried out, during which all distances are counted. Finally, the calculated distances are averaged over the number of iterations and subjected to subsequent transformations. This number of iterations was determined during the tests. It was decided that such a facial scan does not take too long and the results obtained are most reliable. Variants with a larger number of iterations were also tested, but the measurement would take too long and it would be difficult for the user to maintain the same facial expression, e.g. without blinking. Ultimately, all distances are compared to each other in pairs. For example, the distance "Al" from "Ar", etc. The calculated differences between the corresponding distances are then summed and averaged by the number of distances on one side.

## Screenshots

GUI of the app
Main screen <br />
![gui_main](https://github.com/everinho/FaceThePalsy/assets/117845737/cffa7f0a-5dcd-493f-a216-cbcd36c10a58) <br />
Profile view <br />
![profile_view](https://github.com/everinho/FaceThePalsy/assets/117845737/e20d0c0c-8126-40e9-8507-556a445b6929) <br />
Schedule view <br />
![schedule_view](https://github.com/everinho/FaceThePalsy/assets/117845737/de3c5ef2-daaa-425d-bc11-79e9d257ce0f) <br />

Training
Exercise example <br />
![cw1](https://github.com/everinho/FaceThePalsy/assets/117845737/92ec37a6-2115-4305-b7cf-4d8a19b5cf6c) <br />
Exercise example <br />
![cw2](https://github.com/everinho/FaceThePalsy/assets/117845737/43d2ca9d-da6a-485a-b0b2-ca8bb27bd4f1) <br />

## Demo




## Tech Stack

**AI:** Google ML Kit face detection API


## Authors

- [@Antoni Górecki](https://github.com/everinho)

