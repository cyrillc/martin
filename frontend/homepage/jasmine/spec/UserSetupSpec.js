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

    describe("Example Command REnder", function () {
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



});
