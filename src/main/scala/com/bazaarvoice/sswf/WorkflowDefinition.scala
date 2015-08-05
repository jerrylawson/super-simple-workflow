package com.bazaarvoice.sswf

import com.bazaarvoice.sswf.model.{StepResult, StepsHistory}

/**
 * This is where you specify how the workflow executes.
 * @tparam SSWFInput The JVM object representing your workflow input.
 * @tparam StepEnum The enum containing workflow step definitions
 */
trait WorkflowDefinition[SSWFInput, StepEnum <: (Enum[StepEnum] with SSWFStep)] {
  /**
   * Simply return a list of the workflow steps to execute.
   * @param input The input to the workflow
   */
  def workflow(input: SSWFInput): java.util.List[StepEnum]

  /**
   * A hook that gets called when the workflow fails. Use this for example to send
   * a notification with an explanation of what happened.
   * @param input The workflow input
   * @param history The history of what happened in the workflow execution.
   * @param message A summary of the failure. This is what we also send to SWF as the workflow "result".
   */
  def onFail(input: SSWFInput, history: StepsHistory[SSWFInput, StepEnum], message: String): Unit


  /**
   * A hook that gets called when the workflow completes successfully. Use this for example to send
   * a notification that the workflow is complete.
   * @param input The workflow input
   * @param history The history of what happened in the workflow execution.
   * @param message A description of the workflow result. This is what we also send to SWF as the workflow "result".
   */
  def onFinish(input: SSWFInput, history: StepsHistory[SSWFInput, StepEnum], message: String): Unit

  /**
   * Run whatever behaviour needs to be run for the given step.
   * There is a recommended strategy for writing these things: see the Readme (TODO link)
   * @param step The action to take next
   * @param input The input to the workflow
   * @return The outcome of the execution.
   */
  def act(step: StepEnum, input: SSWFInput): StepResult
}