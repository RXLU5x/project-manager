import React, 
{ 
    useState 
} from 'react'

export default function MessageResponse(props){
    const [visible, setVisible] = useState(0);

    let setNotVisible = ()=>{
        setVisible(false);
    }

    function getErrorMessage(errorBody){
        return(
            <div class="ui error message" >
                <i class="close icon" onClick={setNotVisible}></i>
                <div class="header">
                    Error!
                </div>
                <ul class="list">
                    <li>{JSON.stringify(errorBody)}</li>
                </ul>

            </div>
      )
    }

    function getSuccessMessage(successBody, link){
            return(
                <div class="ui success message">
                    <i class="close icon" onClick={setNotVisible}></i>
                    <div class="header">
                        Success!
                    </div>
                    <ul class="list">
                        <li>{JSON.stringify(successBody)}</li>
                        <li><a href={link}>Link</a></li>
                    </ul>
                    
                </div>
        )
        }
    if(visible === false){
        return null
    }
    else if( props.errorResponse && props.errorResponse.error === true){
        console.log(props.errorResponse)
        return getErrorMessage(props.errorResponse.errorBody);
    }
    else if( props.normalResponse && props.normalResponse.success === true){
        return getSuccessMessage(props.normalResponse.successBody, props.normalResponse.link);
    }
    else
        return null;
}