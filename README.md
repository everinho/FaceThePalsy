
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
![asymetria_1](https://github.com/everinho/FaceThePalsy/assets/117845737/4fa42989-38e4-4111-abd9-df26518e82a8)
The following facial distances are then calculated.
![asymetria_2](https://github.com/everinho/FaceThePalsy/assets/117845737/34dca897-15d3-4dbf-b0cb-35ad7dea048c)
![asymetria_3](https://github.com/everinho/FaceThePalsy/assets/117845737/d420d5e3-e5a7-45a6-af6e-688401ead7c0)
![asymetria_4](https://github.com/everinho/FaceThePalsy/assets/117845737/60cc8f30-7f4d-4d9a-85d9-a72301023e86)
In order to minimize possible interference and errors in the measurement, 30 iterations are carried out, during which all distances are counted. Finally, the calculated distances are averaged over the number of iterations and subjected to subsequent transformations. This number of iterations was determined during the tests. It was decided that such a facial scan does not take too long and the results obtained are most reliable. Variants with a larger number of iterations were also tested, but the measurement would take too long and it would be difficult for the user to maintain the same facial expression, e.g. without blinking. Ultimately, all distances are compared to each other in pairs. For example, the distance "Al" from "Ar", etc. The calculated differences between the corresponding distances are then summed and averaged by the number of distances on one side.

## Screenshots

GUI of the app
Main screen
![gui_main](https://github.com/everinho/FaceThePalsy/assets/117845737/36452491-36e5-4521-a64a-25923e98d005)
Profile view
![profile_view](https://github.com/everinho/FaceThePalsy/assets/117845737/fa47f63a-0714-4a20-b9cd-858d3c73b8b1)
Schedule view
![schedule_view](https://github.com/everinho/FaceThePalsy/assets/117845737/042aef34-dce1-49c7-bce1-601293089a35)

Training
Exercise example
![cw1](https://github.com/everinho/FaceThePalsy/assets/117845737/e2cfc6b2-4f96-43d8-98a1-5c21bf6cbff9)
Exercise example
![cw2](https://github.com/everinho/FaceThePalsy/assets/117845737/c6e7945d-316d-4391-ba6b-0ae6494429b7)

## Demo




## Tech Stack

**AI:** Google ML Kit face detection API


## Authors

- [@Antoni Górecki](https://github.com/everinho)

