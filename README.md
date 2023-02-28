# file_upload_project
# This is a spring boot project that uses JDK17.
# This project exposes 4 api endpoints : 
1. Post : /api/file/upload : this api accepts a text file saves it and returns 
{
    "message": "File ${fileName} uploaded successfully.",
    "data": {
        "fileName": ${fileName},
        "lineNumber": ${randomIndex},
        "mostOccurringLetter": ${randomIndex}
    }
}

2. Get : /api/file/all/lines/backward : this api returns one random line backward from all saved files 
{
    "message": "Getting List of backward lines is done successfully.",
    "data": {
        "list": [
            {
                "fileName": ${fileName},
                "lineNumber": ${randomIndex},
                "backwardLine": ${backwardLine}
            }
        ]
    }
}

3. Get : /api/file/all/lines/longest : this api retunrns a list of top 100 lloges lines fromall saved files
{
    "message": "Getting Longest 100 lines of all files is done successfully.",
    "data": {
        "list": [
            "line1",
            "line2",
            ... "line100"
        ]
    }
}

4. Get : /api/file/random/longest : this api returns 20 longest lines from a random saved file 
{
    "message": "Getting Longest 20 lines from ${fileName} is done successfully.",
    "data": {
        "list": [
            "line1",
            "line2",
            ... "line20"
        ]
    }
}
