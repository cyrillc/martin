describe("MArtIn Frontend", function () {
    describe("Martin Render", function () {
        var spy;
        var martinResponseRenderer;
        var martinStatement;

        beforeEach(function () {
            martinResponseRenderer = new MartinResponseRenderer();
            spyOn($.fn, "append");
        });

        it("should append command and response if called", function () {
            martinStatement = {
                request: { command: "command" },
                response: { content: "response" }
            };

            martinResponseRenderer.renderResponse(martinStatement);
            expect($.fn.append.calls.count()).toBe(2);
            expect($.fn.append).toHaveBeenCalledWith("command");
            expect($.fn.append).toHaveBeenCalledWith("response");
        });
    });

    describe("Example Command Render", function () {
        var spy;
        var exampleCommandsRenderer;
        var exampleCommand;

        beforeEach(function () {
            exampleCommand = [
                {
                    call: "call1",
                    description: "desc1"
                },
                {
                    call: "call2",
                    description: "desc2"
                }
            ];
            exampleCommandsRenderer = new ExampleCommandsRenderer(exampleCommand);

            spyOn($.fn, "append");
        });

        it("should append a list of exampleCommands if called", function () {


            exampleCommandsRenderer.renderCommands();
            expect($.fn.append.calls.count()).toBe(4);
            expect($.fn.append).toHaveBeenCalledWith("call1");
            expect($.fn.append).toHaveBeenCalledWith("desc1");
        });
    });

    describe("History Renderer", function () {
        var spy, historyRenderer, historyList, historyItem1, historyItem2;

        beforeEach(function () {
            historyItem1 = {
                id: 1,
                date: 1461768927000,
                request: {
                    id: 1,
                    command: "EASTEREGG"
                },
                response: {
                    id: 1,
                    content: "No"
                }
            };
            historyItem2 = {
                id: 2,
                date: 1461769000000,
                request: {
                    id: 2,
                    command: "doSomething"
                },
                response: {
                    id: 2,
                    content: "No"
                }
            }

            historyList = [historyItem1, historyItem2];
            historyRenderer = new HistoryRenderer(historyList);

            spyOn($.fn, "append");
        });
    });

});
