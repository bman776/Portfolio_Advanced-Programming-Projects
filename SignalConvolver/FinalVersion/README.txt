main FFT convolution program compiled with:
	C:\msys64\ucrt64\bin\g++ convolve.cpp WaveReader.cpp WaveWriter.cpp FFTConvolve.cpp -o convolve.exe -I C:\mysys64\mingw64\include -I .
and executed with:
	./convolve inputSignal.wav impulseResponse.wav outputFile.wav
--------------------------------------------------------------------------------------------------------------------------------------------

BasicConvolveProfiled.cpp and FFTconvolveProfiled.cpp are alternaitve main functions that also use 
	WaveReader.cpp WaveWriter.cpp FFTConvolve.cpp (or BasicConvolve.cpp)
but these were used strictly for the purposes of profilling BasicConvolve.cpp and FFTconvolve.cpp respectively 
--------------------------------------------------------------------------------------------------------------------------------------------


