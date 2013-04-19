// Debugger Web API
// Author: ntietz
// Date:   April 19 2013

var currentline = 1;
var userId = 1;
var debuggerId = 1;

function showresponse(data)
{
    alert(JSON.stringify(data));
}

function failhandler(call, textStatus, errorThrown)
{
    alert("Error: the request failed!");
    alert(JSON.stringify(call));
    alert(textStatus);
    alert(errorThrown);
}

function clearHighlightedLines()
{
    for (var i = 0; i < codeEditor.lineCount(); i++)
    {
        var line = codeEditor.getLineHandle(i);
        codeEditor.removeLineClass(line, "background", "capstone-errorline-background");
        codeEditor.removeLineClass(line, "background", "capstone-currentline-background");
    }
}

function highlightErrorLine(linenumber)
{
    var line = codeEditor.getLineHandle(linenumber-1);
    codeEditor.addLineClass(line, "background", "capstone-errorline-background");
}

function highlightErrors(data)
{
    clearHighlightedLines();
    outputBox.setValue(Array(data.errors.length).join('\n'));

    for (var i = 0; i < data.errors.length; i++)
    {
        highlightErrorLine(data.errors[i].linenumber);
        // you can also use data.errors[i].errortext to display the text.
        outputBox.setLine(i, "Line " + data.errors[i].linenumber + ": " + data.errors[i].errortext);
    }
}

function setCurrentLine(linenumber)
{
    var oldline = codeEditor.getLineHandle(currentline-1);
    codeEditor.removeLineClass(oldline, "background", "capstone-currentline-background");
    var line = codeEditor.getLineHandle(linenumber-1);
    codeEditor.addLineClass(line, "background", "capstone-currentline-background");
    currentline = linenumber;
}

function compile()
{
    var content = codeEditor.getValue()
    submitCommand("prepare", content, function(data) {
        highlightErrors(data);
        //getLineNumber();
    });
}

function run()
{
    submitCommand("run", "", showresponse);
}

function giveInput()
{
    var content = inputEditor.getValue();
    submitCommand("giveinput", content, showresponse);
}

function getStdOut()
{
    submitCommand("getstdout", "", showresponse);
}

function setBreakpoint()
{
    linenumber = $("#linenumber").val();
    submitCommand("breakpoint", linenumber, showresponse);
}

function getLineNumber()
{
    submitCommand("linenumber", "", function(data) {
        setCurrentLine(data.linenumber);
    });
}

function stepIn()
{
    submitCommand("stepin", "", getLineNumber);
}

function stepOver()
{
    submitCommand("stepover", "", getLineNumber);
}

function stepOut()
{
    submitCommand("stepout", "", getLineNumber);
}

function killDebugger()
{
    submitCommand("killdebugger", "", showresponse);
}

function submitCommand(call, data, callback)
{
    $.post("http://localhost:6789",
        {
            "call"  : call,
            "usrid" : userId,
            "dbgid" : debuggerId,
            "data"  : data
        }, callback, "json").fail(failhandler);
}

/*
var codeEditor = CodeMirror.fromTextArea(document.getElementById("source"), {
    mode: "text/x-csrc",
    lineNumbers: true,
    indentUnit: 4,
    tabMode: "shift",
    matchBrackets:true,
});
codeEditor.setSize("80em", "20em");

var inputEditor = CodeMirror.fromTextArea(document.getElementById("input"), {
    smartIndent: false
});
inputEditor.setSize("80em", "5em");
*/

